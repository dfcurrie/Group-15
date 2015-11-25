% Notations
% +: Shows that the argument must be instantiated.
% -: Shows that the argument must be a variable (will be instantiated if the built-in predicate succeeds).
% ?: Shows the argument can be instantiated or a variable.
% strict: The result, may not have duplicate values.
% non-strict: The result can contain duplicate values.
% bonus: Implementation is optional and successful implementation is rewarded with extra points.
% fact vs. rules: Consult lecture notes for the difference between the two.

parentOf(A, B) :- hasChild(A, X).
motherOf(A, B) :- hasChild(A, X), female(A).
fatherOf(A, B) :- hasChild(A, X), male(A).
grandparentOf(A, B) :- parentOf(A, X), parentOf (X, B).
grandmotherOf(A, B) :- parentOf(A, X), motherOf(X, B).
grandfatherOf(A, B) :- parentOf(A, X), fatherOf(X, B).
%greatgrandparentOf(A, B).
%greatgrandmotherOf(A, B).                            
%greatgrandfatherOf(A, B).                            
childOf(A, B) :- parentOf(B, A).
daughterOf(A, B) :- parentOf(B, A), female(A).
sonOf(A, B) :- parentOf(B, A), male(A).
%grandchildOf(A, B).
%granddaughterOf(A, B).
%grandsonOf(A, B).
%greatgrandchildOf(A, B).
%greatgranddaughterOf(A, B).
%greatgrandsonOf(A, B).
%ancestorOf(A, B) :- parentOf(A, B).
%ancestorOf(A, B, N).
%descendantOf(A, B, N).
%related(A, B).
%parent(A).
%childless(A).
%hasChildren(A, L).
%countChildren(A, N).
%sibling(X, Y).
%sisterOf(A, B).
%brotherOf(A, B).
%stepSibling(A, B).
%stepSisterOf(A, B).
%stepBrotherOf(A, B).
%cousin(A, B).
%getSpecies(A, B).
%pet(A).
%feral(A).
