#-*-coding:utf-8-*-

import MySQLdb
import numpy
import time

db = MySQLdb.connect("localhost","root","","geo_data")
db1 = MySQLdb.connect("localhost","root","","v1")
cur = db.cursor()
cur1 = db1.cursor()
usercnt = 0
cnt = 0
s = ""

try:

    user = cur.execute("select uid, Name, Province, City from users_hz")
    info = cur.fetchmany(user)
    usercnt = 0
    sqlu = "insert into poisRecommend_data_user values(%d,'%s',%d,%d)"
    for ii in info:
        s=sqlu%(ii[0],ii[1],ii[2],ii[3])
        #print s
        cur1.execute(s)
        usercnt = usercnt + 1

    print usercnt


    poi = cur.execute("select pid, Titile, Categorys, Lat, Lon, Address, CheckinNum, CheckinUserNum, TodoNum, TipNum from pois_hz")
    info = cur.fetchmany(poi)
    poicnt = 0
    sqlp = "insert into poisManage_poi values('%s', \"%s\", '%s', '%s', '%s', \"%s\", null, %ld, %ld, %ld, %ld)"
    for ii in info:
        s = sqlp%(ii[0], ii[1], ii[2], ii[3], ii[4], ii[5], ii[6], ii[7],ii[8], ii[9])
        #print s
        cur1.execute(s)
        poicnt = poicnt + 1

    print poicnt

    check = cur.execute("select uid, pid, checkin_time from checkin")
    info = cur.fetchmany(check)
    checkcnt = 0
    sqlc = "insert into poisRecommend_check values(%d, '%s', '%s', %d)"
    for ii in info:
        x = time.localtime(int(ii[2])/1000)
        ctime = time.strftime('%Y-%m-%d %H:%M:%S',x)
        s = sqlc% (checkcnt, ctime, ii[1], ii[0])
        #print s
        cur1.execute(s)
        checkcnt = checkcnt+1

    print checkcnt

    cur.close()
    cur1.close()
    db1.commit()
    db.commit()


except:
    print s
    db.rollback()
    db1.rollback()
db1.close()
db.close()