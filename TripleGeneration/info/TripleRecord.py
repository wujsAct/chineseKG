# -*- coding: utf-8 -*-
'''
Time: 2017-7-26
'''

class TripleRecord(object):
  def __init__(self,ent1,ent2,rel):
    self.ent1 = ent1
    self.ent2 = ent2
    self.rel = rel
    
    self.strategy = 'nearArguments'
  
    self.score = 0
    
  def computeScore(self):
    return 0
  
  
  def printTriple(self):
    print '('+self.ent1.strs +","+self.rel.strs+','+self.ent2.strs+","+self.strategy+')'
    
  def isEntInTriple(self,ent):
    if ent is self.ent1:
      return 'left'
    elif ent is self.ent2:
      return 'right'
    else:
      return None
    
  def isRelInTriple(self,rel):
    if rel is self.rel:
      return 'rel'
    else:
      return None