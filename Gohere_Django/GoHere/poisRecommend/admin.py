from django.contrib import admin

# Register your models here.
from .models import Check, data_user

admin.site.register(Check)
admin.site.register(data_user)
