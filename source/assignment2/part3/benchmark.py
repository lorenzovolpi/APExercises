# def block_dec(_func=None, *, block=10):
#     def block_num_dec(g1):
#         def g2(*args, **kwargs):
#             l = []
#             for v in g1(*args, **kwargs):
#                 l.append(v)
#                 if(len(l) == block):
#                     yield l
#                     l = []
#             if(len(l) > 0): yield l
#         return g2
    
#     if _func is None:
#         return block_num_dec
#     else:
#         return block_num_dec(_func)
import time

def benchmark(_func=None, *, warmups=0, iter=1, verbose=False, csv_file=None):
    def benchmark_dec(f):
        def benchmark_wrap(*args, **kwargs):
            #TODO: benchmark
            warmups_times = []
            for _ in range(0, warmups):
                start_time = time.perf_counter  
                f(*args, **kwargs)
                end_time = time.perf_counter
                run_time = end_time - start_time
                warmups_times.append(run_time)
            
            iter_times = []
            for _ in range(0, iter):
                start_time = time.perf_counter  
                f(*args, **kwargs)
                end_time = time.perf_counter
                run_time = end_time - start_time
                iter_times.append(run_time)

            if verbose:
                for i in range(0, warmups):
                    print("Warmup #" + str(i+1) + " took: " + str(warmups_times[i]))
                for i in range(0, iter):
                    print("Iteration #" + str(i+1) + " took: " + str(iter_times[i]))

            average = sum(iter_times) / iter
            variance = sum([(i - average) ** 2 for i in iter_times]) / iter

            print("| Iterations\t| Warmups\t| Average\t| Variance\t|")
            print("| " + iter + "\t| " + warmups + "\t| " + average + "\t| " + variance + "\t|")

        return benchmark_wrap
    
    if _func is None:
        return benchmark_dec
    else:
        return benchmark_dec(_func)