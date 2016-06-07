# -*- coding: utf-8 -*-

from userManage.models import MyUser
from poisRecommend.models import Check, data_user
from poisManage.models import POI
import MySQLdb
import numpy
import pickle
import getopt
import sys
import math
import os

def rad(d):
    return (d*math.pi/180.0)
'''
rec = recommend(user.uid, lat, lon, radius)
radius ~ km
'''
def rec_poi(uid, lat, lon, radius):
    basedir = os.getcwd()
    pkl_file = open(basedir+"/data/dictuser.pkl",'rb')
    dictuser = pickle.load(pkl_file)
    usercnt = len(dictuser)
    pkl_file = open(basedir+"/data/dictpoi.pkl",'rb')
    dictpoi = pickle.load(pkl_file)
    pkl_file = open(basedir+"/data/dictp.pkl",'rb')
    dictp = pickle.load(pkl_file)
    poicnt = len(dictp)
    pkl_file = open(basedir+"/data/edge.pkl",'rb')
    e = pickle.load(pkl_file)
    pkl_file = open(basedir+'/data/poidist.pkl','rb')
    poidist = pickle.load(pkl_file)

    now = dictuser[uid]
    package = now / 5000
    filename = "./data/mat%d.pkl"%(package)
    pkl_file = open(filename,'rb')
    mat = pickle.load(pkl_file)
    now = now % 5000
    value = mat[now]

    is_check = numpy.zeros(poicnt,dtype=int)
    EARTH = 6378.137
    qqqq = 0
    for (i,a) in poidist.iteritems():
        x = float(a[0])
        y = float(a[1])
        radlat1 = rad(lat)
        radlat2 = rad(x)
        b = radlat1 - radlat2
        c = rad(lon) - rad(y)
        s = 2*math.asin(math.sqrt(math.pow(math.sin(b/2),2) + math.cos(radlat1)*math.cos(radlat2)*math.pow(math.sin(b/2),2)))
        s = s*EARTH
        if (s<radius):
            is_check[dictp[i]]=1
            qqqq += 1
        else:
            is_check[dictp[i]]=0

    print qqqq
    edge = set()
    for (i,j,a) in e:
        max = 0
        for ii in a:
            if max < value[ii]:
                max = value[ii]
        if is_check[j]:
            edge.add((i,j,max))

    user_auth = numpy.zeros((2,usercnt)) + 1
    poi_pop = numpy.zeros((2,poicnt)) + 1

    flag = 0
    steps = 10
    for step in xrange(steps):
        user_auth[(flag+1)%2] = numpy.zeros((1,usercnt))
        poi_pop[(flag+1)%2] = numpy.zeros((1,poicnt))
        for (i,j,w) in edge:
            user_auth[(flag + 1)%2,i]+=w*poi_pop[flag,j]
            poi_pop[(flag+1)%2,j]+=w*user_auth[flag,i]
        flag = (flag+1)%2
        #print step

    rank = numpy.argsort(-poi_pop[flag])
    dictpR = dict((v,k) for k,v in dictp.iteritems())
    ans = []
    for i in xrange(20):
        ans.append(dictpR[rank[i]])
        #print poi_pop[flag,rank[i]]
    return ans