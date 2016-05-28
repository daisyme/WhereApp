# -*- coding: utf-8 -*-
from django.contrib import messages
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.core.urlresolvers import reverse
from django.http import HttpResponseRedirect, JsonResponse, HttpResponse, HttpRequest
from django.template import RequestContext, loader
from django.shortcuts import render, get_object_or_404
from django.views.decorators.http import require_POST
from django.contrib.auth import authenticate
from django.core.exceptions import PermissionDenied
from rest_framework import viewsets, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser

from poisRecommend.serializers import d_userSerializer
from poisRecommend.models import data_user
from .serializers import UserSerializer , UserInfoSerializer
from .forms import LoginForm, RegisterForm
from .models import MyUser
import simplejson

class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

@permission_classes((IsAuthenticated, ))
class UserViewSet(viewsets.ModelViewSet):
    serializer_class = UserSerializer
    queryset = MyUser.objects.all()
    lookup_field = 'id'
    #queryset = News.objects.all()
    def get_queryset(self):
        if not 'search' in self.request.GET:
            ft = MyUser.objects.all()
        else:
            ft = MyUser.objects.filter(title__contains=self.request.GET['search'])
        return ft.order_by('-id')

@api_view(['GET','POST'])
@permission_classes((IsAuthenticated, ))
def example_view(request, format=None):
    content = {
        'status': 'request was permitted'
    }
    return Response(content)

#显示或更新用户信息
@api_view(['GET', 'PUT'])
def user_detail(request, pk):
    try:
        user = MyUser.objects.get(pk=pk)
    except MyUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        user_info = UserInfoSerializer(user)
        return JSONResponse(user_info.data)

    elif request.method == 'PUT':
        req=simplejson.loads(request.body)
        user_info = UserInfoSerializer(user, data=req)
        if user_info.is_valid():
            user_info.save()
            return Response("修改成功")
        return JSONResponse(user_info.errors, status=status.HTTP_400_BAD_REQUEST)

#创建用户
@api_view(['POST','GET'])
def user_list(request):
        """
        建立新的user或展示所有用户
        """
        if request.method == 'GET':
            users = MyUser.objects.all()
            user = UserSerializer(users, many=True)
            return JSONResponse(user.data)

        elif request.method == 'POST':
            req=simplejson.loads(request.body)
            username = req['username']
            email = req['email']
            password = req['password']
            b = MyUser.objects.filter(username=username)
            if(len(b) != 0):
                return HttpResponse("用户名已存在！")
            new_user = MyUser.objects.create_user(username,email,password)
            id = new_user.id
            d_user =  data_user.objects.create(id=id,username=username)
            return HttpResponse("创建成功")

# 登录
@api_view(['POST'])
def user_login(request):
    if request.method == 'POST':
        req=simplejson.loads(request.body)
        username = req['username']
        password = req['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                login(request,user)
                dict = {}
                dict["avatar"]=unicode(user.avatar)
                dict["email"]=user.email
                dict["id"]=user.id
                return JSONResponse(dict)
            else:
                return HttpResponse("用户被冻结啦！快求我！")
        else:
            return HttpResponse("用户名或者密码不正确！")


#登出
@api_view(['POST','GET'])
def user_logout(request):
    logout(request)
    return HttpResponse("客官拜拜啦您~")