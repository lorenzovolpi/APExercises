module Ex1 where

    data ListBag a = LB [(a, Int)]
        deriving (Show, Eq)

    checkSingle :: Eq a => [a] -> ListBag a -> Bool
    checkSingle acc (LB ((x, i):xs)) = (not (elem x acc)) && (checkSingle (x:acc) (LB xs))
    checkSingle acc (LB []) = True

    insertReorder :: Eq t => [t] -> t -> [t]
    insertReorder [] x = [x]
    insertReorder (a:acc) x 
        | x == a = x:a:acc
        | otherwise = a:(insertReorder acc x)

    reorderList :: Eq t => [t] -> [t] -> [t]
    reorderList acc [] = acc
    reorderList acc (x:xs) = reorderList (insertReorder acc x) xs

    insertLB :: Eq a => ListBag a -> a -> ListBag a
    insertLB (LB []) x = LB [(x, 1)]
    insertLB (LB ((a, i):acc)) x
        | (x == a) = (LB ((a, i+1):acc))
        | otherwise = LB ((x, 1):(a, i):acc)

    accLB :: Eq a => ListBag a -> [a] -> ListBag a
    accLB acc [] = acc
    accLB acc (x:xs) = accLB (insertLB acc x) xs

    addCP :: (Eq t1, Num t) => [(t1, t)] -> (t1, t) -> [(t1, t)]
    addCP [] (b, j) = [(b, j)]
    addCP ((a, i):acc) (b, j)
        | a == b = (a, i+j):acc
        | otherwise = (a, i):(addCP acc (b, j))

    sumLB :: Eq a => ListBag a -> ListBag a -> ListBag a
    sumLB (LB lba) (LB []) = (LB lba)
    sumLB (LB lba) (LB (l:lbb)) =
        sumLB (LB (addCP lba l)) (LB lbb)

    fw :: Eq a => ListBag a -> ListBag a
    fw lb = sumLB empty lb
    
    wf :: Eq a => ListBag a -> Bool
    wf lb = checkSingle [] lb

    empty :: ListBag a
    empty = LB []

    singleton :: a -> ListBag a
    singleton v = LB [(v, 1)]

    fromList :: Eq a => [a] -> ListBag a
    fromList lst = accLB empty l
        where l = reorderList [] lst

    isEmpty :: ListBag t -> Bool
    isEmpty (LB lst) = case lst of
        [] -> True
        x:xs -> False

    mul :: Eq a => a -> ListBag a -> Int
    mul v (LB []) = 0
    mul v (LB ((a, i):lb))
        | v == a = i 
        | otherwise = mul v (LB lb)

    toList :: ListBag a -> [a]
    toList (LB []) = []
    toList (LB ((a, i):lb)) 
        | i > 0 = a:(toList (LB ((a, i-1):lb)))
        | otherwise = toList (LB lb)

    sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
    sumBag bag bag'
        | wf bag = sumLB bag bag'
        | otherwise = sumLB (fw bag) bag'

        