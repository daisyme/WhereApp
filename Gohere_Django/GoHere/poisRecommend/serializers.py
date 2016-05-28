# -*- coding: utf-8 -*-
from .models import Check, data_user, Todo, Like
from rest_framework import serializers


class CheckSerializer(serializers.ModelSerializer):
    class Meta:
        model = Check
        #fields = ('username', 'email', 'groups')

class d_userSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        """docstring for Meta"""
        model = data_user

class TodoSerializer(serializers.ModelSerializer):
    class Meta:
        """docstring for Meta"""
        model = Todo

class LikeSerializer(serializers.ModelSerializer):
    class Meta:
        """docstring for Meta"""
        model = Like
            