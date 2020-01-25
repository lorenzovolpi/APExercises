import Data.Char (toUpper)

-- ex01
myReplicate n v 
    | n <= 0 = []
    | otherwise = v:(myReplicate (n-1) v)
    
myReplicate' n v = replicate n v

--ex02
sumOdd (x:xs)
    | (odd x) = x + (sumOdd xs)
    | otherwise = sumOdd xs
sumOdd [] = 0

sumOdd' xs = foldl (+) 0 $ filter odd xs

--ex03
repl (x:xs) n = myReplicate n x ++ (repl xs n)
repl [] n = []

repl' xs n = foldl (\x -> \y -> x ++ y) [] $ map (myReplicate n) xs

--ex04
totalLength (x:xs) 
    | (head x) == 'A' = (length x) + totalLength xs
    | otherwise = totalLength xs
totalLength [] = 0

totalLength' xs = foldl (+) 0 $ map length $ filter (\s -> (head s) == 'A') xs

--ex05
filterOdd xs = foddpos xs 0
    where 
        foddpos (z:zs) n 
            | (odd n) = z:(foddpos zs (n+1))
            | otherwise = foddpos zs (n+1)
        foddpos [] n = []
    
filterOdd' xs = map (\(a,b) -> a) $ filter (\(a,b) -> odd b) $ map (\x -> (last (take (x+1) xs), x)) $ take (length xs) [0..]

--ex06
titlecase s = unwords $ upper (words s)
    where
        upper (x:xs) = ((toUpper (head x)):(tail x)):(upper xs)
        upper [] = []

titlecase' s = unwords $ map (\x -> (toUpper (head x)):(tail x)) $ words s

--ex07
countVowelPali (x:xs) 
    | pali x = (cvowel x) + (countVowelPali xs)
    | otherwise = countVowelPali xs
    where
        pali s = (s == reverse s)
        cvowel (s:sz)
            | isvowel s = 1 + cvowel sz
            | otherwise = cvowel sz
            where
                isvowel s1 = elem s1 ['a', 'e', 'i', 'o', 'u']
        cvowel [] = 0
countVowelPali [] = 0

countVowelPali' xs = foldl (+) 0 $ map (\x -> length x) $ map (\x -> filter (\y -> elem y ['a', 'e', 'i', 'o', 'u']) x) $ filter (\x -> (x == reverse x)) xs

--ex08
mapl f a = foldl (\x -> \y -> x ++ [(f y)]) [] a
mapr f a = foldr (\x -> \y -> (f x):y) [] a

--ex09
data IntTree = Leaf Int | Node (Int, IntTree, IntTree)
    deriving(Show)

tmap f t = case t of
    Node(x, lt, rt) -> Node(f x, tmap f lt, tmap f rt)
    Leaf x -> Leaf (f x)

succTree t = tmap succ t

sumSucc t = sumTree $ succTree t
    where 
        sumTree t1 = case t1 of
            Node(x, lt, rt) -> x + (sumTree lt) + (sumTree rt)
            Leaf x -> x

--test
main = do
    print (myReplicate 4 5)
    print (myReplicate' 4 5)
    print (sumOdd [1,2,3,4,5,6,7])
    print (sumOdd' [1,2,3,4,5,6,7])
    print (repl [1,2,3] 4)
    print (repl' [1,2,3] 4)
    print (totalLength ["Amico", "Friend", "Amaca"])
    print (totalLength' ["Amico", "Friend", "Amaca"])
    print (filterOdd [1,2,3,4,5,6,7])
    print (filterOdd' [1,2,3,4,5,6,7])
    print (titlecase "red fox jumped over lazy dog")
    print (titlecase' "red fox jumped over lazy dog")
    print (countVowelPali ["anna", "banana", "civic", "mouse"])
    print (countVowelPali' ["anna", "banana", "civic", "mouse"])
    print (mapl ((+) 2) [1,2,3,4,5])
    print (mapr ((+) 2) [1,2,3,4,5])
    print (sumSucc $ Node(4, Node(1, Leaf 2, Leaf 3), Leaf 5))
