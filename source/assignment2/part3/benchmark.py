import time
import csv
import threading

def benchmark(_func=None, *, warmups=0, iter=1, verbose=False, csv_file=None):
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
                for i in range(0, warmups):
                    print("Warmup #" + str(i+1) + " took: " + str(warmups_times[i]))
                for i in range(0, iter):
                    print("Iteration #" + str(i+1) + " took: " + str(iter_times[i]))

            average = sum(iter_times) / iter
            variance = sum([(i - average) ** 2 for i in iter_times]) / iter

            print("| Iter\t| Warm\t| Aver\t| Var\t|")
            print("| " + str(iter) + "\t| " + str(warmups) + "\t| " + str(average) + "s\t| " + str(variance) + "s\t|")

            if csv_file is not None:
                with open(csv_file, 'w', newline='') as csvf:
                    csv_writer = csv.writer(csvf, delimiter=',')
                    csv_writer.writerow(["run num", "is warmup", "timing"])
                    for i in range(0, warmups):
                        csv_writer.writerow([str(i+1), "yes", str(warmups_times[i]) + "s"])
                    for i in range(0, iter):
                        csv_writer.writerow([str(i+1), "no", str(iter_times[i]) + "s"])

        return benchmark_wrap
    
    if _func is None:
        return benchmark_dec
    else:
        return benchmark_dec(_func)

def fibo(n):
    if n <= 0:
        return 0
    elif n == 1:
        return 1
    else:
        return fibo(n - 1) + fibo(n - 2)

class fibo_call:
    def __init__(self, n):
        self.n = n

    def __call__(self):
        fibo(self.n)

def th_aux(nthreads, fibo_par):
    clb = fibo_call(fibo_par)
    ths = []
    for _ in range(0, nthreads):
        ths.append(threading.Thread(target=clb))

    for th in ths:
        th.start()

    for th in ths:
        th.join()

def test(fibo_par):

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