# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

# Create your models here.

class POI(models.Model):
    pid = models.CharField(primary_key=True, max_length=30)
    name = models.CharField(max_length=100)
    category = models.CharField(max_length=20)
    lat = models.CharField(max_length=30)
    lon = models.CharField(max_length=30)
    address = models.CharField(max_length=100, null = True, blank = True)
    desc = models.CharField(max_length=500, null = True, blank = True)
    checkinNum = models.IntegerField(default=0)
    checkinUserNum = models.IntegerField(default=0)
    todoNum = models.IntegerField(default=0)
    likeNum = models.IntegerField(default=0)
    def __unicode__ (self):
        return self.name

class Cat(models.Model):
    pid = models.ForeignKey(POI)
    cat = models.IntegerField(default=0)