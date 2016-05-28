# -*- coding: utf-8 -*-
from .models import MyUser
from rest_framework import serializers


class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = MyUser
        fields = ('email', 'city', 'province', 'desc', 'gender', 'date_of_birth')
    def validate_email(self, value):
        """
        Check that the blog post is about Django.
        """
        if '@' not in value.lower():
            raise serializers.ValidationError("The email is not True")
        return value

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = MyUser
        fields = ('id','username', 'email', 'city', 'province', 'desc', 'gender', 'date_of_birth', 'avatar')
    def validate_username(self, value):
        """
        检验用户名
        """
        if 'django' not in value.lower():
            raise serializers.ValidationError("用户名已存在！")
        return value

class Login(serializers.ModelSerializer):
    class Meta:
        model = MyUser
        fields = ('username', 'password')
