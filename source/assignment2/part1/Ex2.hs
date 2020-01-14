module Ex2 where

    import Ex1

    foldr :: Foldable t => (a -> b -> b) -> b -> t a -> b
    instance Foldable ListBag where
        foldr f z (LB []) = z
        foldr f z (LB ((a, i):lb)) =
            f a (foldr f z (LB lb))

    mapLB :: Eq a => (t -> a) -> ListBag t -> ListBag a
    mapLB f (LB lb)
        | wf mapped = mapped
        | otherwise = fw mapped
        where
            lbmap f [] = []
            lbmap f ((a, i):as) =
                (f a, i):(lbmap f as)
            mapped = LB (lbmap f lb)

    -- Per garantire che il risultato della funzione mapLB sia well-formed è 
    -- necessario poter effettuare delle comparazioni tra gli elementi della
    -- ListBag risultante. Per tale ragione è necessario imporre il vincolo "Eq a"
    -- nella definizione di mapLB, vincolo non previsto da fmap del class construsctor
    -- Functor. Perciò non è possibile assegnare mapLB a fb per instanziare Functor.