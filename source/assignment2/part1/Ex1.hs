module Ex1 where

    data ListBag a = LB [(a, Int)]
        deriving (Show, Eq)

    -- Funzione di supporto per wf che controlla se un elemento è 
    -- presente in un ListBag solo una volta.
    checkSingle :: Eq a => [a] -> ListBag a -> Bool
    checkSingle acc (LB ((x, i):xs)) = (not (elem x acc)) && (checkSingle (x:acc) (LB xs))
    checkSingle acc (LB []) = True

    -- Funzione che dato un elemento e una lista, lo inserisce
    -- nella lista subito prima del primo elemento ad esso uguale o
    -- in fondo altrimenti
    insertReorder :: Eq t => [t] -> t -> [t]
    insertReorder [] x = [x]
    insertReorder (a:acc) x 
        | x == a = x:a:acc
        | otherwise = a:(insertReorder acc x)

    -- Funzione che riordina una lista affiancando gli elementi
    -- uguali
    reorderList :: Eq t => [t] -> [t] -> [t]
    reorderList acc [] = acc
    reorderList acc (x:xs) = reorderList (insertReorder acc x) xs

    -- Funzione di support per accLB che inserisce un elemento
    -- all'interno di un ListBag
    insertLB :: Eq a => ListBag a -> a -> ListBag a
    insertLB (LB []) x = LB [(x, 1)]
    insertLB (LB ((a, i):acc)) x
        | (x == a) = (LB ((a, i+1):acc))
        | otherwise = LB ((x, 1):(a, i):acc)

    -- Funzione di supporto per fromList che aggiunge
    -- gli elementi di una lista all'interno di un ListBag
    accLB :: Eq a => ListBag a -> [a] -> ListBag a
    accLB acc [] = acc
    accLB acc (x:xs) = accLB (insertLB acc x) xs

    -- Funzione che data una lista di coppie (a, Int) e una
    -- coppia ulteriore aggiunge quest'ultima alla lista se
    -- la coppia non è già presente, altrimenti somma le molteplicità
    -- se a è già nella lista all'interno di una coppia.
    addCP :: (Eq t1, Num t) => [(t1, t)] -> (t1, t) -> [(t1, t)]
    addCP [] (b, j) = [(b, j)]
    addCP ((a, i):acc) (b, j)
        | a == b = (a, i+j):acc
        | otherwise = (a, i):(addCP acc (b, j))

    -- Funzione di supporto per sumBag che esegue l'unione di 
    -- due ListBag senza eseguire controlli
    sumLB :: Eq a => ListBag a -> ListBag a -> ListBag a
    sumLB (LB lba) (LB []) = (LB lba)
    sumLB (LB lba) (LB (l:lbb)) =
        sumLB (LB (addCP lba l)) (LB lbb)

    -- Funzione che a partire da un ListBag ne restituisce
    -- un altro equivalente con la garanzia che questo
    -- sia well-formed
    fw :: Eq a => ListBag a -> ListBag a
    fw lb = sumLB empty lb
    
    -- Funzione che controlla se un ListBag passato come
    -- parametro è well-formed
    wf :: Eq a => ListBag a -> Bool
    wf lb = checkSingle [] lb

    -- Funzione che restituisce un ListBag vuoto.
    empty :: ListBag a
    empty = LB []

    -- Funzione che crea un ListBag singoletto da
    -- un elemento passato come parametro
    singleton :: a -> ListBag a
    singleton v = LB [(v, 1)]

    -- Funzione che genera un ListBag a partire da una lista
    fromList :: Eq a => [a] -> ListBag a
    fromList lst = accLB empty l
        where l = reorderList [] lst

    -- Funzione che restituisce true su un ListBag è vuoto
    isEmpty :: ListBag t -> Bool
    isEmpty (LB lst) = case lst of
        [] -> True
        x:xs -> False

    -- Funzione che restituisce la molteplicità di un elemento
    -- all'interno di un ListBag
    mul :: Eq a => a -> ListBag a -> Int
    mul v (LB []) = 0
    mul v (LB ((a, i):lb))
        | v == a = i 
        | otherwise = mul v (LB lb)

    -- Funzione che a partire da un ListBag restituisce una lista
    -- dove ogni elemento è ripetuto con la molteplicità che ha nel
    -- ListBAg di partenza
    toList :: ListBag a -> [a]
    toList (LB []) = []
    toList (LB ((a, i):lb)) 
        | i > 0 = a:(toList (LB ((a, i-1):lb)))
        | otherwise = toList (LB lb)

    -- Funzione che unisce due ListBag sommando le molteplicità degli elementi
    -- uguali
    sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
    sumBag bag bag'
        | wf bag = sumLB bag bag'
        | otherwise = sumLB (fw bag) bag'

        