# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.contrib.auth.models import AbstractUser, UserManager
from django.db import models



class MyUser(AbstractUser):
    # username = models.CharField(max_length=30)
    # surname = models.CharField(max_length=30)
    # email = models.EmailField(max_length=40)
    # password = models.CharField(max_length=80)
    city = models.IntegerField(default=0)
    province = models.IntegerField(default=0)
    desc = models.CharField(max_length=400, null=True)   # intro
    gender = models.CharField(max_length=2, choices=[('m', '男'), ('f', '女')], null=True, blank=True, verbose_name='性别')
    date_of_birth = models.DateField(blank=True, null=True, verbose_name='出生日期')
    avatar = models.ImageField(upload_to='avatar/%Y/%m/%d', default='avatar/default.png', blank=True, verbose_name='头像')
    likeNum = models.IntegerField(default=0)
    todoNum = models.IntegerField(default=0)
    doneNum = models.IntegerField(default=0)
    
    def __unicode__(self):
        return unicode(self.username)

    #can be divide into two models: UserProfile

