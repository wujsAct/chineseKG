# -*- coding: utf-8 -*-
'''
Time: 2017-7-26
'''

class PhraseRecord(object):
  def __init__(self,start,end,pattern=None):
    self.startIndex = start
    self.endIndex = end
    self.pattern = pattern
    self.strs= ''
    
    
  def getIndexes(self):
    return self.startIndex, self.endIndex
    
  def getPattern(self):
    return self.pattern
  def getContent(self):
    return self.content
        
  def setContent(self,strs):
    self.strs = strs
  
  def printstr(self):
    print self.strs

class RelRecord(PhraseRecord):
  def __init__(self, start, end, pattern=None):
    PhraseRecord.__init__(self, start, end, pattern)
    self.hasEntPair = False
  def printstr(self):
    print 'rel:',self.strs
    
class EntRecord(PhraseRecord):
  def __init__(self, start, end, pattern=None):
    PhraseRecord.__init__(self, start, end, pattern)
    self.hasRel = False
  def printstr(self):
    print 'ent:',self.strs