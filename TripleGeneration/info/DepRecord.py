# -*- coding: utf-8 -*-
"""
Created on Mon Jul 31 10:10:14 2017

@author: wujs

@dependency triple
"""

class DepRecord(object):
  def __init__(self,depStr):
    #print depStr
    source,rel,target = depStr.split('\t\t')
    
    self.source = {}
    self.target = {}
    self.rel = rel
    
    self.source['index'] = int(source.split('\t')[1])
    self.source['strs'] = source.split('\t')[0]
    
    self.target['index'] = int(target.split('\t')[1])
    self.target['strs'] = target.split('\t')[0]
    
  def printTriple(self):
    print '('+self.source['strs'] +","+self.rel+','+self.target['strs']+')'
    