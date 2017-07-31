# -*- coding: utf-8 -*-
"""
Created on Wed Jul 26 15:25:19 2017

@author: wujs
"""
from info.DepRecord import DepRecord
from info.PhraseRecord import PhraseRecord,EntRecord,RelRecord
from info.TripleRecord import TripleRecord
from steps.tripleExtract import InfoExtraction


import codecs
import json

with codecs.open('data/demo.des','r','utf-8') as file:
  for line in file:
    doc = json.loads(line)
    for sent_item in doc:
      
      sent = sent_item[0]; ments = sent_item[1]; rels = sent_item[2]
      deps = sent_item[3]
      
      mentsRecord_list=[];relsRecord_list=[];depsRecord_list=[]
      
      for dep in deps:
        depRecord = DepRecord(dep)
        depsRecord_list.append(depRecord)
      
      for key in ments:
        entRecord = EntRecord(int(key[1]),int(key[2]))
        entRecord.setContent(key[0])
        #entRecord.printstr()
        mentsRecord_list.append(entRecord)
        
      for key in rels:
        relRecord = RelRecord(int(key[1]),int(key[2]))
        relRecord.setContent(key[0])
        #relRecord.printstr()
        relsRecord_list.append(relRecord)
        
      infoExtractor = InfoExtraction(entRecord_list=mentsRecord_list,relRecord_list=relsRecord_list,dep_triple=depsRecord_list,sent=sent)
      
      infoExtractor.mergeDepAndNR()
      
      for triple in infoExtractor.tripleRecords:
        triple.printTriple()
      
      print 'deps:'
      for triple in infoExtractor.new_tripleRecords:
        triple.printTriple()
      print '---------------------------'
      
    print 'new -------------------------------'  