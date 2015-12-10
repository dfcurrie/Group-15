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
greatgrandchildOf(A, B) :- grandchildOf(X, B), parent(X, A).
greatgranddaughterOf(A, B) :-greatgrandchildOf(A, B), female(A).
greatgrandsonOf(A, B) :- greatgrandchildOf(A, B), male(A).
ancestorOf(A, B) :- parentOf(A, B), A\=B.
ancestorOf(A, B) :- parentOf(A, X), ancestorOf(X, B), A\=B.
descendantOf(A, B) :- childOf(A, X), descendantOf(X, B), A\=B.
ancestorOf(A, B, N) :- N=0, A=B; N=1, parentOf(A, B); N=2, grandparentOf(A, B); N=3, greatgrandparentOf(A, B).
descendantOf(A, B, N) :- N=0, A=B; N=1, childOf(A, B); N=2, grandchildOf(A, B); N=3, greatgrandchildOf(A, B).
related(A, B) :- A=B; ancestorOf(A,B); descendantOf(A, B).
parent(A) :- parentOf(A, X).
childless(A) :- \+(parent(A)).
hasChildren(A, L) :- findall(B, childOf(B, A), L).
listlength([],0).
listlength([H|T],N)  :-  listlength(T,X),  N  is  X+1.
countChildren(A, N) :- hasChildren(A, L), listlength(L,N).
sibling(A, B) :- parentOf(X, A), parentOf(X, B), parentOf(Y, A), parentOf(Y, B), X\=Y, A\=B.
sisterOf(A, B) :- sibling(A, B), female(A).
brotherOf(A, B) :- sibling(A, B), male(A).
stepSibling(A, B) :- parentOf(X, A), parentOf(X, B), parentOf(Y, A), parentOf(Z, B), Y\=Z, A\=B, \+sibling(A,B).
stepSisterOf(A, B) :- stepSibling(A, B), female(A).
stepBrotherOf(A, B) :- stepSibling(A, B), male(A).
cousin(A, B) :- parentOf(X,A), parentOf(Y,B), sibling(X,Y), A\=B.

%getSpecies(A, B) :- species(A, B) ; ((hasChild(X, A), getSpecies(X, B)) ; ((parentOf(X, A)), getSpecies(X, B))).
getSpecies(A, B) :- species(A, B) ; ( ((((hasChild(X, A))) ; ((hasChild(A, X)))), getSpecies(X, B))). 
pet(A) :- owns(X, A), getSpecies(A, cat) ; owns(X, A), getSpecies(A, dog).
feral(A) :- (getSpecies(A, cat) ; getSpecies(A, dog)), (\+(owns(X, A))).

%getSpecies(A, B) :- species(A, B) ; (((hasChild(X, A)), (species(X, B) ; getSpecies(X, B))),! ; ((parentOf(A, X)), (species(X, B) ; getSpecies(X, B))),!).

%getSpecies(A, B) :- species(A, B) ; (((hasChild(X, A)), (species(X, B) ; getSpecies(X, B))),! ; ((parentOf(A, X)), (species(X, B) ; getSpecies(X, B))),!).

%getSpecies(A, B) :- (species(A, C), C\=B,!,fail) ; (species(A, B) ; X\=A, hasChild(X, A))
