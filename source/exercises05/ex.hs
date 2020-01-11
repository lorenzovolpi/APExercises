import Data.Maybe (Maybe)

data Var = X | Y | Z
data Expr a = Const a | Sum (Expr a) (Expr a) | Mul (Expr a) (Expr a) | Div (Expr a) (Expr a) | Id Var

--ex01
eval e = case e of
    Const a -> a
    Sum e1 e2 -> (eval e1) + (eval e2)
    Mul e1 e2 -> (eval e1) * (eval e2)

--ex02
safeEval e = case e of
    Const a -> Just a
    Sum e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> Just (a+b)
        (_, _) -> Nothing
        where 
            ev1 = safeEval e1
            ev2 = safeEval e2
    Mul e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> Just (a*b)
        (_, _) -> Nothing
        where 
            ev1 = safeEval e1
            ev2 = safeEval e2
    Div e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> 
            if b == 0 then Nothing
            else Just (div a b)
        (_, _) -> Nothing
        where 
            ev1 = safeEval e1
            ev2 = safeEval e2

--ex03
instance Functor Expr where
    fmap f (Const a) = Const (f a)
    fmap f (Sum e1 e2) = Sum (fmap f e1) (fmap f e2)
    fmap f (Mul e1 e2) = Mul (fmap f e1) (fmap f e2)
    fmap f (Div e1 e2) = Div (fmap f e1) (fmap f e2)
    
--ex04
instance Foldable Expr where
    foldr f z (Const a) = f a z
    foldr f z (Sum e1 e2) = foldr f (foldr f z e2) e1
    foldr f z (Mul e1 e2) = foldr f (foldr f z e2) e1
    foldr f z (Div e1 e2) = foldr f (foldr f z e2) e1

--ex05
subst (x, y, z) e = case e of
    Const a -> Const a
    Sum e1 e2 -> (Sum (subst (x, y, z) e1) (subst (x, y, z) e2))
    Mul e1 e2 -> (Mul (subst (x, y, z) e1) (subst (x, y, z) e2))
    Div e1 e2 -> (Div (subst (x, y, z) e1) (subst (x, y, z) e2))
    Id v -> case v of
        X -> subst (x, y, z) x
        Y -> subst (x, y, z) y
        Z -> subst (x, y, z) z

veval e = case e of
    Const a -> Just a
    Sum e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> Just (a+b)
        (_, _) -> Nothing
        where 
            ev1 = veval e1
            ev2 = veval e2
    Mul e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> Just (a*b)
        (_, _) -> Nothing
        where 
            ev1 = veval e1
            ev2 = veval e2
    Div e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> 
            if b == 0 then Nothing
            else Just (div a b)
        (_, _) -> Nothing
        where 
            ev1 = veval e1
            ev2 = veval e2
    Id v -> Nothing
    
recEval (x, y, z) e = case e of 
    Const a -> Just a
    Sum e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> Just (a+b)
        (_, _) -> Nothing
        where 
            ev1 = recEval (x, y, z) e1
            ev2 = recEval (x, y, z) e2
    Mul e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> Just (a*b)
        (_, _) -> Nothing
        where 
            ev1 = recEval (x, y, z) e1
            ev2 = recEval (x, y, z) e2
    Div e1 e2 -> case (ev1, ev2) of
        (Just a, Just b) -> 
            if b == 0 then Nothing
            else Just (div a b)
        (_, _) -> Nothing
        where 
            ev1 = recEval (x, y, z) e1
            ev2 = recEval (x, y, z) e2
    Id v -> case v of
        X -> recEval (x, y, z) x
        Y -> recEval (x, y, z) y
        Z -> recEval (x, y, z) z

--ex06
instance Show Var where
    show X = "x"
    show Y = "y"
    show Z = "z"

instance (Show a) => Show(Expr a) where
    show (Const a) = (show a)
    show (Sum e1 e2) = "(" ++ (show e1) ++ " + " ++ (show e2) ++ ")"
    show (Mul e1 e2) = "(" ++ (show e1) ++ " * " ++ (show e2) ++ ")"
    show (Div e1 e2) = "(" ++ (show e1) ++ " / " ++ (show e2) ++ ")"
    show (Id v) = (show v)

--ex07
evalPrint e = print (eval e)

evalPrintSub e = (eps e) >>= print
    where
        eps (Const a) = do
            return a
        eps (Sum e1 e2) = do
            ev1 <- eps e1
            print ev1
            ev2 <- eps e2
            print ev2
            return (ev1 + ev2)
        eps (Mul e1 e2) = do
            ev1 <- eps e1
            print ev1
            ev2 <- eps e2
            print ev2
            return (ev1 * ev2)
--test            
main = do
    print (eval (Sum (Mul (Const 2) (Const 3)) (Const 4)))
    print (safeEval (Div (Sum (Mul (Const 2) (Const 3)) (Const 4)) (Const 0)))
    print (safeEval (Div (Sum (Mul (Const 2) (Const 3)) (Const 4)) (Const 5)))
    print (safeEval $ fmap ((*) 2) (Div (Sum (Mul (Const 2) (Const 3)) (Const 4)) (Const 5)))
    print (safeEval $ fmap ((*) 2) (Div (Sum (Mul (Const 2) (Const 3)) (Const 4)) (Const 0)))
    print (foldr (\x -> \y -> x:y) [] (Div (Sum (Mul (Const 2) (Const 3)) (Const 4)) (Const 5)))
    print (veval $ subst (Const 2, Const 3, Const 4) (Div (Sum (Mul (Id X) (Id Y)) (Id Z)) (Id X)))
    print (recEval (Const 2, Const 3, Const 4) (Div (Sum (Mul (Id X) (Id Y)) (Id Z)) (Id X)))
    print (show $ Div (Sum (Mul (Const 2) (Const 3)) (Const 4)) (Const 5))
    evalPrint (Sum (Mul (Const 2) (Const 3)) (Const 4))
    evalPrintSub (Sum (Mul (Const 2) (Const 3)) (Const 4))
