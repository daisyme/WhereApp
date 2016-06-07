from django.test import TestCase, Client

# Create your tests here.
from .models import Check, data_user
class SimpleTest(TestCase):
    def test_basic_addition(self):
#Tests that 1 + 1 always equals 2.

        self.assertEqual(1 + 1, 2)

    def test_pois_404(self):
        response = self.client.get('/poi/3')
        self.assertEqual(response.status_code, 404)
    def test_pois_200(self):
        response = self.client.get('/POI')
        self.assertEqual(response.status_code, 200)
    def test_example(self):
        post = {}

    def test_register_and_login(self):
        post = {"username": "newUser", "email": "XXX@XXX.XXX", "password": "XXXXXXX"}
        response = self.client.post('/register', post)
        self.assertEqual(response.status_code, 201)
        response = self.client.get('/profile/1')
        self.assertEqual(response.status_code,200)
        response = self.client.post('/login', {"username":"newUser","password":"XXXXXXX"})
        self.assertEqual(response.status_code,200)
