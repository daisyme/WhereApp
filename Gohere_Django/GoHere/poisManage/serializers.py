# -*- coding: utf-8 -*-
from .models import POI
from rest_framework import serializers


class POISerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = POI
        fields = ('pid','name','category', 'lat', 'lon', 'address', 'desc', 'checkinNum', 'checkinUserNum', 'likeNum', 'todoNum')

class POIInfo(serializers.ModelSerializer):
    class Meta:
        model = POI
        fields = ('name', 'lat', 'lon', 'address', 'desc', 'checkinNum', 'checkinUserNum', 'likeNum', 'todoNum')