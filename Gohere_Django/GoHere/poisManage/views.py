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

from .serializers import POISerializer
from .models import POI

class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

@permission_classes((IsAuthenticated, ))
class POIViewSet(viewsets.ModelViewSet):
    serializer_class = POISerializer
    queryset = POI.objects.all()
    #lookup_field = 'pid'
#queryset = News.objects.all()
    def get_queryset(self):
        if not 'search' in self.request.GET:
            ft = POI.objects.all()
        else:
            ft = POI.objects.filter(title__contains=self.request.GET['search'])
        return ft.order_by('-pid')

#显示//或更新新的地点信息
@api_view(['GET'])
def POI_detail(request, pk):
    try:
        poi = POI.objects.get(pk=pk)
    except POI.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        poi_info = POISerializer(poi)
        return JSONResponse(poi_info.data, status=status.HTTP_200_OK)