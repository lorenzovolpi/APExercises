module Ex1 (
    wf,
    empty,
    singleton,
    fromList,
    isEmpty,
    mul,
    toList,
    sumBag) where

    data ListBag a = LB [(a, Int)]
        deriving (Show, Eq)

    checkSingle acc (LB ((x, i):xs)) = (not (elem x acc)) && (checkSingle (x:acc) (LB xs))
    checkSingle acc (LB []) = True

    insertReorder [] x = [x]
    insertReorder (a:acc) x 
        | x == a = x:a:acc
        | otherwise = a:(insertReorder acc x)

    reorderList acc [] = acc
    reorderList acc (x:xs) = reorderList (insertReorder acc x) xs

    insertLB (LB []) x = LB [(x, 1)]
    insertLB (LB ((a, i):acc)) x
        | (x == a) = (LB ((a, i+1):acc))
        | otherwise = LB ((x, 1):(a, i):acc)

    accLB acc [] = acc
    accLB acc (x:xs) = accLB (insertLB acc x) xs

    addCP [] (b, j) = [(b, j)]
    addCP ((a, i):acc) (b, j)
        | a == b = (a, i+j):acc
        | otherwise = (a, i):(addCP acc (b, j))

    wf lb = checkSingle [] lb

    empty = LB []

    singleton v = LB [(v, 1)]

    fromList lst = accLB empty l
        where l = reorderList [] lst

    isEmpty (LB lst) = case lst of
        [] -> True
        x:xs -> False

    mul v (LB []) = 0
    mul v (LB ((a, i):lb))
        | v == a = i 
        | otherwise = mul v (LB lb)

    toList (LB []) = []
    toList (LB ((a, i):lb)) 
        | i > 0 = a:(toList (LB ((a, i-1):lb)))
        | otherwise = toList (LB lb)

    sumBag (LB lb1) (LB []) = LB lb1
    sumBag (LB lb1) (LB (l:lb2)) =
        sumBag (LB (addCP lb1 l)) (LB lb2)

    -- main = do
    --     print $ wf $ LB [("d", 3), ("k", 7)]
    --     print $ wf $ LB [("d", 3), ("k", 7), ("d", 1)]
    --     print $ fromList [1,2,3,1,2,1,3,1,1,2,3,1,2,1,3,4,1,2,3,4,1,3,2,1,1,1,2]  
    --     print $ wf $ fromList [1,2,3,1,2,1,3,1,1,2,3,1,2,1,3,4,1,2,3,4,1,3,2,1,1,1,2]  
    --     print $ isEmpty (LB [])      
    --     print $ isEmpty (singleton "d")
    --     print $ mul 3 $ fromList [1,2,3,1,2,1,3,1,1,2,3,1,2,1,3,4,1,2,3,4,1,3,2,1,1,1,2]
    --     print $ mul 6 $ fromList [1,2,3,1,2,1,3,1,1,2,3,1,2,1,3,4,1,2,3,4,1,3,2,1,1,1,2]
    --     print $ toList $ fromList [1,2,3,1,2,1,3,1,1,2,3,1,2,1,3,4,1,2,3,4,1,3,2,1,1,1,2]
    --     print $ sumBag (LB [("d", 3), ("k", 7)]) (LB [("d", 1)])
    --     print $ sumBag (LB [("d", 3), ("k", 7)]) (LB [("d", 3), ("k", 7), ("d", 1)])
