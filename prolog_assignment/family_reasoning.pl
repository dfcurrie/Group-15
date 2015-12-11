parentOf(A, B) :- hasChild(A, B).
motherOf(A, B) :- hasChild(A, B), female(A).
fatherOf(A, B) :- hasChild(A, B), male(A).
grandparentOf(A, B) :- parentOf(A, X), parentOf(X, B).
grandmotherOf(A, B) :- grandparentOf(A, B), female(A).
grandfatherOf(A, B) :- grandparentOf(A, B), male(A).
greatgrandparentOf(A, B) :- grandparentOf(A, X), parentOf(X, B).
greatgrandmotherOf(A, B) :- greatgrandparentOf(A, B), female(A).
greatgrandfatherOf(A, B) :- greatgrandparentOf(A, B), male(A).
childOf(A, B) :- parentOf(B, A).
daughterOf(A, B) :- childOf(A, B), female(A).
sonOf(A, B) :- childOf(A, B), male(A).
grandchildOf(A, B) :- parentOf(B,X), parentOf(X,A).
granddaughterOf(A, B) :- grandchildOf(A, B), female(A).
grandsonOf(A, B) :- grandchildOf(A, B), male(A).
greatgrandchildOf(A, B) :- grandchildOf(X, B), parentOf(X, A).
greatgranddaughterOf(A, B) :-greatgrandchildOf(A, B), female(A).
greatgrandsonOf(A, B) :- greatgrandchildOf(A, B), male(A).
ancestorOf(A, B) :- parentOf(A, B), A\=B.
ancestorOf(A, B) :- parentOf(A, X), ancestorOf(X, B), A\=B.
descendantOf(A, B) :- childOf(A, B), A\=B.
descendantOf(A, B) :- childOf(A, X), descendantOf(X, B), A\=B.
ancestorOf(A, B, N) :- N=0, A=B; N=1, parentOf(A, B); N=2, grandparentOf(A, B); N=3, greatgrandparentOf(A, B).
descendantOf(A, B, N) :- N=0, A=B; N=1, childOf(A, B); N=2, grandchildOf(A, B); N=3, greatgrandchildOf(A, B).
related(A, B) :- setof(X, (A=B; ancestorOf(A,B); descendantOf(A, B)),L).
parent(A) :- setof(X, parentOf(A, X),L).
childless(A) :- isanimal(A), \+parent(A).
hasChildren(A, L) :- findall(B, childOf(B, A), L).
listlength([],0).
listlength([H|T],N)  :-  listlength(T,X),  N  is  X+1.
countChildren(A, N) :- hasChildren(A, L), listlength(L,N).
sibling(A, B) :- setof(X, Y^(parentOf(X, A), parentOf(X, B), parentOf(Y, A), parentOf(Y, B), X\=Y, A\=B), L).
sisterOf(A, B) :- sibling(A, B), female(A).
brotherOf(A, B) :- sibling(A, B), male(A).
stepSibling(A, B) :- setof(X, Y^(fatherOf(X, A), motherOf(Y, A), fatherOf(W, B), motherOf(Z, B), parentOf(S, A), parentOf(S, B), parentOf(T, A), \+parentOf(T, B)),L).
stepSisterOf(A, B) :- stepSibling(A, B), female(A).
stepBrotherOf(A, B) :- stepSibling(A, B), male(A).
cousin(A, B) :- parentOf(X,A), parentOf(Y,B), sibling(X,Y), A\=B.

isanimal(A) :- male(A); female(A).
human(A) :- isanimal(A), \+pet(A), \+feral(A).
dog(A) :- (pet(A); feral(A)), \+species(A, cat).

getSpecies(A, B) :- setof(X, Y^(species(A, B); dog(A), B=dog; human(A), B=human),L).
pet(A) :- setof(X, (isanimal(A), owns(X, A)),L).
feral(A) :- setof(X, Y^(isanimal(A), \+pet(A), (pet(X), related(A, X); pet(X), related(Y, X), related(A, Y))),L).