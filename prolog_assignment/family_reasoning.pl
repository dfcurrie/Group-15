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
grandchildOf(A, B) :- parentOf(B,X), parent(X,A).
granddaughterOf(A, B) :- grandchildOf(A, B), female(A).
grandsonOf(A, B) :- grandchildOf(A, B), male(A).
greatgrandchildOf(A, B) :- grandchildOf(X, B), parent(X, A).
greatgranddaughterOf(A, B) :-greatgrandchildOf(A, B), female(A).
greatgrandsonOf(A, B) :- greatgrandchildOf(A, B), male(A).
ancestorOf(A, B) :- parentOf(A, B).
ancestorOf(A, B) :- parentOf(A, X), ancestorOf(X, B).

%ancestorOf(A, B, N).
%descendantOf(A, B, N).
%related(A, B) :- ancestorOf(A,B)
%parent(A).
% parent(A) :- parentOf(A, X).
%childless(A).
% childless(A) :- parent(A), !.
%hasChildren(A, L).
%countChildren(A, N).
%sibling(X, Y):- parentOf(X, A), parentOf(X, B).
%sibling(A, B) :- parentOf(X, A), parentOf(X, B).

sisterOf(A, B) :- sibling(A, B), female(A).
brotherOf(A, B) :- sibling(A, B), male(A).
%stepSibling(A, B).
%stepSisterOf(A, B).
%stepBrotherOf(A, B).
%cousin(A, B).
%getSpecies(A, B).
%pet(A).
%feral(A).















