# -*- coding: utf-8 -*-
"""
Created on Wed Jul 26 16:02:00 2017

@author: wujs
"""
from info.PhraseRecord import PhraseRecord,EntRecord,RelRecord
from info.TripleRecord import TripleRecord
from collections import defaultdict

'''
@we need to generate triples from those three kinds of infromation
'''
class InfoExtraction(object):
  
  def __init__(self,entRecord_list,relRecord_list,sent,dep_triple,topicEnt=None):
    self.entRecords = entRecord_list
    self.relRecords = relRecord_list
    self.sent = sent
    self.dep_triple = dep_triple
    self.tripleRecords = defaultdict(TripleRecord)
    self.new_tripleRecords = defaultdict(TripleRecord)
    self.topicEntStr = topicEnt  #topic entity string
    
  #2017/7/27 we add dependecy rules to generate the triples
  def dependencyTree(self):
    tripleRecords = []
    
    
    for i in range(len(self.dep_triple)):
      dep = self.dep_triple[i]
      
      dep_Rel = dep.rel
      
      dep_source = dep.source; dep_target = dep.target
      
      ext1_ind = dep_source['index']; ext2_ind = dep_target['index']
      #print 'deps:',ext1_ind,ext2_ind
      ext_record1= None; ext_record2=None
      
      for key in self.relRecords:
        s,e = key.getIndexes()
        if ext1_ind in xrange(s,e+1):
          ext_record1 = key
          break
        
      for key in self.entRecords:
        s,e = key.getIndexes()
        if ext1_ind in xrange(s,e+1):
          ext_record1 = key
          break
        
      for key in self.relRecords:
        s,e = key.getIndexes()
        if ext2_ind in xrange(s,e+1):
          ext_record2 = key
          break
      for key in self.entRecords:
        s,e = key.getIndexes()
        if ext2_ind in xrange(s,e+1):
          ext_record2 = key
          break
      if ext_record1!=None and ext_record2!=None:
        tripleRecords.append([ext_record1,ext_record2,dep_Rel,dep])
      
    return tripleRecords
        
  def nearArgument(self):
    self.tripleRecords = defaultdict(TripleRecord)
    for rel in self.relRecords:
      s_rel,e_rel = rel.getIndexes()
      left_ent = -1; right_ent = -1;
      min_left=10000;min_right = 10000
      for ent_i in range(len(self.entRecords)):
        ent = self.entRecords[ent_i]
        
        if ent.hasRel==False:  
          s_ent,e_ent = ent.getIndexes()
          
          if  s_ent - e_rel >=0 and s_ent - e_rel  < min_right:
            right_ent = ent_i
            min_right = s_ent - e_rel
            
          if s_rel - e_ent >=0 and s_rel - e_ent  < min_left:
            left_ent = ent_i
            min_left = s_rel - e_ent
      
      if left_ent!=-1 and right_ent!=-1:
        self.entRecords[right_ent].hasRel = True
        self.entRecords[left_ent].hasRel = True
        
        triple = TripleRecord(self.entRecords[left_ent],self.entRecords[right_ent],rel)
        triple.strategy = 'nearArgument'
        
        self.tripleRecords[triple] = 1
                          
  
  def mergeDepAndNR(self):
    self.nearArgument()  #获取Reverb near argument 的relation
    
    depTriples = self.dependencyTree()
    
    
    for key in depTriples:
      record1,record2,dep_rel,dep = key

      '''
      @we need to revise or add triples
      '''
      #print dep.printTriple()
      if dep_rel == 'nsubj':
#        print dep_rel
#        print type(record1),type(record2)
#        print record1.strs,record2.strs
#        print dep.printTriple()
        #record1 is the rel and record2 is the left argument
        for triple_key in self.tripleRecords:
          
          if triple_key.isRelInTriple(record1)!=None:
            new_triple = TripleRecord(record2,triple_key.ent2,record1)
            new_triple.strategy='nsubj'
            record2.hasRel = True
            record1.hasEntPair =True
            self.new_tripleRecords[new_triple] = 1
                                  
      if dep_rel == 'dobj':
        #record1 is the rel and record2 is the right argument
        for triple_key in self.tripleRecords:
          if triple_key.isRelInTriple(record1)!=None:
            new_triple = TripleRecord(triple_key.ent1,record2,record1)
            new_triple.strategy='dobj'
            self.new_tripleRecords[new_triple] = 1
            record2.hasRel = True
            record1.hasEntPair =True
              
      if dep_rel =='conj':
        for triple_key in self.tripleRecords:
          if triple_key.isRelInTriple(record1)!=None: 
            if type(record2) == 'RelRecord':
              new_triple = TripleRecord(triple_key.ent1,triple_key.ent2,record2)
              new_triple.strategy='conj'
              self.new_tripleRecords[new_triple] = 1
                                    
        for triple_key in self.tripleRecords:
          if triple_key.isRelInTriple(record2)!=None: 
            if type(record1) == 'RelRecord':
              new_triple = TripleRecord(triple_key.ent1,triple_key.ent2,record1)
              new_triple.strategy='conj'
              self.new_tripleRecords[new_triple] = 1
        


        for triple_key in self.tripleRecords:
          index = triple_key.isEntInTriple(record1)
          if index!=None: 
            if type(record2) == 'EntRecord':
              if index == 'left':
                new_triple = TripleRecord(record2,triple_key.ent2,triple_key.rel)
                new_triple.strategy='conj'
              else:
                new_triple = TripleRecord(triple_key.ent1,record2,triple_key.rel)
                new_triple.strategy='conj'
              self.new_tripleRecords[new_triple] = 1
                                    
        for triple_key in self.tripleRecords:
          index = triple_key.isEntInTriple(record2)
          if index!=None: 
            print type(record1)
            if type(record1) == 'EntRecord':
              if index == 'left':
                new_triple = TripleRecord(record1,triple_key.ent2,triple_key.rel)
                new_triple.strategy='conj'
              else:
                new_triple = TripleRecord(triple_key.ent1,record1,triple_key.rel)
                new_triple.strategy='conj'
              self.new_tripleRecords[new_triple] = 1
        
      if dep_rel =='advcl':
        triple_1 = None;triple_2=None
        for triple_key in self.tripleRecords:
          if triple_key.isRelInTriple(record1)!=None: 
            triple_1 = triple_key
            break
                                      
        for triple_key in self.tripleRecords:
          if triple_key.isRelInTriple(record2)!=None: 
            triple_2 =triple_key
            break
          
        if triple_1 !=None and triple_2!=None:
          new_triple = TripleRecord(triple_1.ent1,triple_2.ent2,triple_2.rel)
          new_triple.strategy='conj'
          self.new_tripleRecords[new_triple] = 1
                                    
        
          
            
        #print '-----------------'