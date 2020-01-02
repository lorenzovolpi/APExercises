def ciao_word(str:str):
    return ''.join(sorted(list(str.lower())))

def add_ciao_dict(ciao_dict:dict, word:str):
    ciao = ciao_word(word)
    if(ciao in ciao_dict):
        ciao_dict[ciao].add(word)
    else:
        s:set = {word}
        ciao_dict[ciao] = s

def create_dict(path:str):
    d = {}
    with open(path, "r") as f:
        for line in f:
            l = line.strip()
            if len(l) > 0:
                add_ciao_dict(d, l)
    return d

def replace_anagrams(d, line):
    ls = line.split(" ")
    res = []
    for w1 in ls:
        ciao = ciao_word(w1)
        if ciao in d:
            s = d[ciao] - {w1}
            w2 = s.pop()
            res.append(w2)
        else:
            res.append(w1)
    return " ".join(res)
            
if __name__ == "__main__":
    import sys
    d = create_dict(sys.argv[1])
    print(replace_anagrams(d, sys.argv[2]))



