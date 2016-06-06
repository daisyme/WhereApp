"""GoHere URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url, include, patterns
from django.contrib import admin
from rest_framework import routers
from django.conf.urls.static import static 
from django.conf import settings

#data
from userManage.views import UserViewSet, example_view, user_detail, user_list, user_login, user_logout
from poisManage.views import POIViewSet, POI_detail
from poisRecommend.views import CheckViewSet, d_userViewSet, user_history, user_like, user_todo, recommend

router = routers.DefaultRouter()
router.register(r'users', UserViewSet)
router.register(r'checks', CheckViewSet)
router.register(r'POI', POIViewSet)
router.register(r'data_users', d_userViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^admin', admin.site.urls),
    url(r'^register', user_list),
    url(r'^login',user_login ),
    url(r'^allusers', user_list),
    url(r'^logout', user_logout),
    url(r'^profile/(?P<pk>.*)', user_detail),
    url(r'^example', example_view),
    url(r'^checkin/(?P<pk>.*)', user_history),
    url(r'^like/(?P<pk>.*)',user_like),
    url(r'^todo/(?P<pk>.*)',user_todo),
    url(r'^poi/(?P<pk>.*)', POI_detail),
    url(r'^result/?P<pk>.*', recommend)

]
urlpatterns += static('public/static', document_root=settings.STATIC_ROOT)
urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
