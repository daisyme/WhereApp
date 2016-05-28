# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from poisManage.models import POI

# Create your models here.

class data_user(models.Model):
    id = models.BigIntegerField(primary_key=True)
    username = models.CharField(max_length=50)
    city = models.IntegerField(default=0, blank=True)
    province = models.IntegerField(default=0, blank=True)
    def __unicode__ (self):
        return self.name


class Check(models.Model):
    uid = models.ForeignKey('data_user')
    pid = models.ForeignKey(POI)
    time = models.DateTimeField(auto_now_add=True)
    def __unicode__(self):
        return self.time

class Like(models.Model):
    uid = models.ForeignKey('data_user')
    pid = models.ForeignKey(POI)

class Todo(models.Model):
    uid = models.ForeignKey('data_user')
    pid = models.ForeignKey(POI)