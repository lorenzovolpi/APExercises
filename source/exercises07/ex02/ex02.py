def block_dec(_func=None, *, block=10):
    def block_num_dec(g1):
        def g2(*args, **kwargs):
            l = []
            for v in g1(*args, **kwargs):
                l.append(v)
                if(len(l) == block):
                    yield l
                    l = []
            if(len(l) > 0): yield l
        return g2
    
    if _func is None:
        return block_num_dec
    else:
        return block_num_dec(_func)

def block_ten_dec(g1):
    def g2(*args, **kwargs):
        l = []
        for v in g1(*args, **kwargs):
            l.append(v)
            if(len(l) == 10):
                yield l
                l = []
        if(len(l) > 0): yield l
    return g2

@block_dec(block=5)
def fib_gen(n):
    f1 = 0
    f2 = 1
    for _ in range(n):
        yield f2
        f1, f2 = f2, f1 + f2

if __name__ == "__main__":
    import sys
    for l in fib_gen(int(sys.argv[1])):
        for v in l:
            print(str(v), end=" ")
        print()