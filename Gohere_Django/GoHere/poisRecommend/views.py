# -*- coding: utf-8 -*-
from django.contrib import messages
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, JsonResponse, HttpResponse
from django.template import RequestContext, loader
from django.shortcuts import render, get_object_or_404
from django.views.decorators.http import require_POST
from rest_framework import serializers,viewsets, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser

from .serializers import CheckSerializer, d_userSerializer, LikeSerializer, TodoSerializer
from .models import Check, data_user, Like, Todo
from utils.algorithm import rec_poi
from userManage.models import MyUser
from poisManage.models import POI
from poisManage.serializers import POIInfo

import simplejson


class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

@permission_classes((IsAuthenticated, ))
class CheckViewSet(viewsets.ModelViewSet):
    serializer_class = CheckSerializer
    queryset = Check.objects.all()
#queryset = News.objects.all()
    def get_queryset(self):
        if not 'search' in self.request.GET:
            ft = Check.objects.all()
        else:
            ft = Check.objects.filter(title__contains=self.request.GET['search'])   #可扩展功能！
        return ft.order_by('-id')

@permission_classes((IsAuthenticated, ))
class d_userViewSet(viewsets.ModelViewSet):
    serializer_class = d_userSerializer
    queryset = data_user.objects.all()
#queryset = News.objects.all()
    def get_queryset(self):
        if not 'search' in self.request.GET:
            ft = data_user.objects.all()
        else:
            ft = data_user.objects.filter(title__contains=self.request.GET['search'])
        return ft.order_by('-id')

#显示或更新用户历史记录（即去过的地点）
@api_view(['GET', 'PUT'])
def user_history(request, pk):
    try:
        user = data_user.objects.get(pk=pk)
    except data_user.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        check = Check.objects.filter(uid=user.uid)
        user_his = CheckSerializer(check, many=True)
        return JSONResponse(user_his.data, status=status.HTTP_200_OK)
    elif request.method == 'PUT':
        req=simplejson.loads(request.body)
        try:
            poi = POI.objects.get(pk=req['pid'])
        except POI.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        check = Check()
        check.uid = user
        check.pid = poi
        myuser = MyUser.objects.get(pk=pk)
        myuser.doneNum += 1
        myuser.save()
        poi.checkinNum += 1
        b = Check.objects.filter(uid_id=user.uid)
        if(len(b) == 0):
            poi.checkinUserNum += 1
        poi.save()
        check.save()
        dict = {}
        dict["checkinUser"] = poi.checkinUserNum
        dict["checkin"] = poi.checkinNum
        return JSONResponse(dict, status=status.HTTP_200_OK)


# 展示或更新喜欢去的地点
@api_view(['GET', 'PUT'])
def user_like(request, pk):
    try:
        user = data_user.objects.get(pk=pk)
    except data_user.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        like = Like.objects.filter(uid=user.uid)
        #print like
        poi = POI.objects.filter(pk=0)
        for i in like:
            #print i.pid_id
            poi = POI.objects.filter(pk = i.pid_id) | poi
        poi_like = POIInfo(poi, many=True)
        return JSONResponse(poi_like.data, status=status.HTTP_200_OK)
    elif request.method == 'PUT':
        req=simplejson.loads(request.body)
        try:
            poi = POI.objects.get(pk=req['pid'])
        except POI.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        b = Like.objects.filter(uid=user.uid).filter(pid=req['pid']).exists()
        myuser = MyUser.objects.get(pk=pk)
        if(b):
            nolike = Like.objects.filter(uid=user.uid).get(pid=req['pid'])
            nolike.delete()
            myuser.likeNum -= 1
            poi.likeNum -= 1
        else:
            myuser.likeNum += 1
            poi.likeNum += 1
            like = Like()
            like.uid = user
            like.pid = poi
            like.save()
        myuser.save()
        poi.save()
        dict = {}
        dict["poi_like"] = poi.likeNum
        dict["user_like"] = myuser.likeNum
        return JSONResponse(dict, status=status.HTTP_200_OK)

# 展示或更新想去的地点
@api_view(['GET', 'PUT'])
def user_todo(request, pk):
    try:
        user = data_user.objects.get(pk=pk)
    except data_user.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)


    if request.method == 'GET':
        todo = Todo.objects.filter(uid=user.uid)
        #print todo
        poi = POI.objects.filter(pk=0)
        for i in todo:
            #print i.pid_id
            poi = POI.objects.filter(pk = i.pid_id) | poi
        poi_todo = POIInfo(poi, many=True)
        return JSONResponse(poi_todo.data, status=status.HTTP_200_OK)
    elif request.method == 'PUT':
        req=simplejson.loads(request.body)
        try:
            poi = POI.objects.get(pk=req['pid'])
        except POI.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        b = Todo.objects.filter(uid=user.uid).filter(pid=req['pid']).exists()
        myuser = MyUser.objects.get(pk=pk)
        if(b):
            notodo = Todo.objects.filter(uid=user.uid).get(pid=req['pid'])
            notodo.delete()
            myuser.todoNum -= 1
            poi.todoNum -= 1
        else:
            todo = Todo()
            todo.uid = user
            todo.pid = poi
            todo.save()
            poi.todoNum += 1
            myuser.todoNum += 1
        myuser.save()
        poi.save()
        dict = {}
        dict["poi_todo"] = poi.todoNum
        dict["user_todo"] = myuser.todoNum
        return JSONResponse(dict, status=status.HTTP_200_OK)


@api_view(['POST'])
def poi_info(request, pk):
    try:
        user = data_user.objects.get(pk=pk)
    except data_user.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    req=simplejson.loads(request.body)
    try:
        poi=POI.objects.get(pk=req['pid'])
    except POI.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    a = Todo.objects.filter(uid=user.uid).filter(pid=req['pid']).exists()
    b = Like.objects.filter(uid=user.uid).filter(pid=req['pid']).exists()
    c = Check.objects.filter(uid=user.uid).filter(pid=req['pid']).exists()
    dict = {}
    dict["todo"] = a
    dict["like"] = b
    dict["check"] = c
    dict["poi_name"] = poi.name
    dict["address"] = poi.address
    dict["checkinNum"] = poi.checkinNum
    dict["checkinUserNum"] = poi.checkinUserNum
    dict["likeNum"] = poi.likeNum
    dict["todoNum"] = poi.todoNum
    return JSONResponse(dict, status=status.HTTP_200_OK)


@api_view(['POST'])
def recommend(request, pk):
    try:
        user = data_user.objects.get(pk=pk)
    except data_user.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    req=simplejson.loads(request.body)
    lat=float(req['lat'])
    lon=float(req['lon'])
    radius=req['radius']

    rec = rec_poi(user.uid, lat, lon, radius)

    poi = POI.objects.filter(pk=0)
    for i in rec:
        poi = POI.objects.filter(pk = i) | poi
    poi_rec = POIInfo(poi, many=True)
    return JSONResponse(poi_rec.data, status=status.HTTP_200_OK)

