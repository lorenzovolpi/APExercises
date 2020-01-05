def fib_gen(n):
    f1 = 1
    f2 = 1
    for _ in range(n):
        yield f1
        f1, f2 = f2, f1 + f2


if __name__ == "__main__":
    import sys
    for i in fib_gen(int(sys.argv[1])):
        print(i)