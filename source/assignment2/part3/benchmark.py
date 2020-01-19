import time
import csv
import threading
import requests
import os

def benchmark(_func=None, *, warmups=0, iter=1, verbose=False, csv_file=None):
    """
    Decorator parametrico che esegue dei warmup prima delle esecuzioni effettive
    della funzione decorata. Calcola poi la media e la varianza delle esecuzioni
    e mostra una tabella riassuntisa sullo standard output. Se richiesto mostra anche
    i tempi ottenuti e li salva su un file.
    """
    def benchmark_dec(f):
        def benchmark_wrap(*args, **kwargs):
            #TODO: benchmark
            warmups_times = []
            for _ in range(0, warmups):
                start_time = time.perf_counter()  
                f(*args, **kwargs)
                end_time = time.perf_counter()
                run_time = end_time - start_time
                warmups_times.append(run_time)
            
            iter_times = []
            for _ in range(0, iter):
                start_time = time.perf_counter()  
                f(*args, **kwargs)
                end_time = time.perf_counter()
                run_time = end_time - start_time
                iter_times.append(run_time)

            if verbose:
                for (i, w) in enumerate(warmups_times):
                    print("Warmup #" + str(i+1) + " took: " + str(w))
                for (i, t) in enumerate(iter_times):
                    print("Iteration #" + str(i+1) + " took: " + str(t))

            average = sum(iter_times) / iter
            variance = sum([(i - average) ** 2 for i in iter_times]) / iter

            print("| Iter\t| Warm\t| Aver\t| Var\t|")
            print("| " + str(iter) + "\t| " + str(warmups) + "\t| " + str(average) + "s\t| " + str(variance) + "s\t|")

            if csv_file is not None:
                with open(csv_file, 'w', newline='') as csvf:
                    csv_writer = csv.writer(csvf, delimiter=',')
                    csv_writer.writerow(["run num", "is warmup", "timing"])
                    for (i, w) in enumerate(warmups_times):
                        csv_writer.writerow([str(i+1), "yes", str(w) + "s"])
                    for (i, t) in enumerate(iter_times):
                        csv_writer.writerow([str(i+1), "no", str(t) + "s"])

        return benchmark_wrap
    
    if _func is None:
        return benchmark_dec
    else:
        return benchmark_dec(_func)

def fibo(n):
    """
    Funzione per il calcolo ricorsivo dell'n-esimo numero di fibonacci
    """
    if n <= 0:
        return 0
    elif n == 1:
        return 1
    else:
        return fibo(n - 1) + fibo(n - 2)

class fibo_call:
    """
    Classe callable per il calcolo dell'n-esimo numero di fibonacci
    """
    def __init__(self, n):
        self.n = n

    def __call__(self):
        fibo(self.n)

def th_aux(nthreads, fibo_par):
    """
    Funzione che esegue la funzione di fibonacci su nthreads paralleli
    contemporaneamente.
    """
    clb = fibo_call(fibo_par)
    ths = []
    for _ in range(0, nthreads):
        ths.append(threading.Thread(target=clb))

    for th in ths:
        th.start()

    for th in ths:
        th.join()

def loadAndExec(url):
    """
    Scarica uno script in python e lo esegue. Salva lo script in un
    file temporaneo e lo elimina dopo l'esecuzione.
    """
    if(url == ""): 
        return

    tmp = "tmp" + str(time.time()) + ".py"
    r = requests.get(url)
    with open(tmp, "w") as f:
        f.write(r.text)
    
    os.system("python3 " + tmp)
    os.remove(tmp)

def prepost(_func=None, *, preurl="", posturl=""):
    """
    Decorator che esegue due script scaricati da due url passati come paramentri,
    prima e dopo l'esecuzione della funzione decorata.
    """
    def prepost_dec(f):
        def prepost_wrap(*args, **kwargs):
            loadAndExec(preurl)
            f(*args, **kwargs)
            loadAndExec(posturl)
        
        return prepost_wrap
    
    if _func is None:
        return prepost_dec
    else:
        return prepost_dec(_func)


@prepost(preurl="http://pages.di.unipi.it/corradini/Didattica/AP-19/PROG-ASS/02/pre.py", posturl="http://pages.di.unipi.it/corradini/Didattica/AP-19/PROG-ASS/02/post.py")
def test(fibo_par):
    """
    Funzione per l'esecuzione dei test richiesti.
    """

    #f-1-16
    f_1_16 = benchmark(th_aux, iter=16, csv_file="f-1-16.csv")
    f_1_16(1, fibo_par)

    #f-2-8
    f_2_8 = benchmark(th_aux, iter=8, csv_file="f-2-8.csv")
    f_2_8(2, fibo_par)

    #f-4-4
    f_4_4 = benchmark(th_aux, iter=4, csv_file="f-4-4.csv")
    f_4_4(4, fibo_par)

    #f-8-2
    f_8_2 = benchmark(th_aux, iter=2, csv_file="f-8-2.csv")
    f_8_2(8, fibo_par)



if __name__ == "__main__":
    test(30)



# Concretamente non si osserva un apparente vantaggio nella parallelizzazione
# delle esecuzioni. Pare, anzi, che ogni thread, nelle esecuzioni multithreaded
# venga eseguito in modo sequenziale. A ciò sì aggiunge l'overhead della creazione
# di nuovi thread, quindi probabilmente l'esecuzione di della funzione su un
# numero minore di thread porta a tempistiche migliori. Ciò è dovuto alla gestione
# del multithreading in python in relazione al garbage collector, che rappresenta
# una delle critiche maggiori a tale linguaggio.