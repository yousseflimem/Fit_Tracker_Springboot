{
  "info": {
    "name": "Demo App - Entity Tests by Package",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    { "key": "baseUrl",       "value": "http://localhost:8082" },
    { "key": "token",         "value": "" },
    { "key": "membershipId",  "value": "" },
    { "key": "coachUserId",   "value": "" },
    { "key": "normalUserId",  "value": "" },
    { "key": "classId",       "value": "" },
    { "key": "workoutId",     "value": "" },
    { "key": "productId",     "value": "" },
    { "key": "bookingId",     "value": "" },
    { "key": "orderId",       "value": "" },
    { "key": "paymentId",     "value": "" },
    { "key": "reviewId",      "value": "" }
  ],
  "item": [
    {
      "name": "Authentication",
      "item": [
        {
          "name": "01 - Register User",
          "request": {
            "method": "POST",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "url": "{{baseUrl}}/api/auth/register",
            "body": { "mode": "raw", "raw": "{\"username\":\"user1\",\"password\":\"pass123\",\"email\":\"user1@example.com\"}" }
          },
          "event": [{ "listen":"test", "script": { "exec":[
            "pm.test('Status 200', ()=>pm.response.to.have.status(200));"
          ]}}]
        },
        {
          "name": "02 - Authenticate User",
          "request": {
            "method": "POST",
            "header": [{ "key": "Content-Type","value":"application/json"}],
            "url":"{{baseUrl}}/api/auth/authenticate",
            "body":{ "mode":"raw", "raw":"{\"username\":\"user1\",\"password\":\"pass123\"}" }
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.test('Status 200', ()=>pm.response.to.have.status(200));",
            "pm.environment.set('token', pm.response.json().token);"
          ]}}]
        }
      ]
    },
    {
      "name": "Membership",
      "item": [
        {
          "name": "01 - Create Membership",
          "request": {
            "method": "POST",
            "header": [
              { "key":"Content-Type","value":"application/json" },
              { "key":"Authorization","value":"Bearer {{token}}" }
            ],
            "url":"{{baseUrl}}/api/memberships",
            "body":{ "mode":"raw","raw":"{\"type\":\"BASIC\",\"price\":29.99,\"duration\":30}" }
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.test('Status 200', ()=>pm.response.to.have.status(200));",
            "pm.environment.set('membershipId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get All Memberships",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/memberships"
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.test('Status 200', ()=>pm.response.to.have.status(200));"
          ]}}]
        },
        {
          "name": "03 - Get Membership By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/memberships/{{membershipId}}"
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.test('Status 200', ()=>pm.response.to.have.status(200));"
          ]}}]
        },
        {
          "name": "04 - Update Membership",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/memberships/{{membershipId}}",
            "body":{"mode":"raw","raw":"{\"type\":\"PREMIUM\",\"price\":49.99,\"duration\":60}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.test('Status 200', ()=>pm.response.to.have.status(200));"
          ]}}]
        },
        {
          "name": "05 - Delete Membership",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/memberships/{{membershipId}}"
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.test('Status 2xx', ()=>pm.expect(pm.response.code).to.be.oneOf([200,204]));"
          ]}}]
        }
      ]
    },
    {
      "name": "User",
      "item": [
        {
          "name": "01 - Create Coach User",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/users",
            "body":{"mode":"raw","raw":"{\"username\":\"coach1\",\"email\":\"coach1@example.com\",\"password\":\"pass123\",\"role\":\"COACH\",\"membershipId\":{{membershipId}}}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('coachUserId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Create Normal User",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/users",
            "body":{"mode":"raw","raw":"{\"username\":\"user2\",\"email\":\"user2@example.com\",\"password\":\"pass123\",\"role\":\"USER\",\"membershipId\":{{membershipId}}}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('normalUserId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "03 - Get All Users",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/users"
          }
        },
        {
          "name": "04 - Get User By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/users/{{normalUserId}}"
          }
        },
        {
          "name": "05 - Update User",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/users/{{normalUserId}}",
            "body":{"mode":"raw","raw":"{\"username\":\"user2-upd\",\"email\":\"u2@ex.com\",\"password\":\"newpass\",\"role\":\"USER\",\"membershipId\":{{membershipId}}}"}
          }
        },
        {
          "name": "06 - Update User Role",
          "request": {
            "method":"PUT",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/users/{{coachUserId}}/role?role=ADMIN"
          }
        },
        {
          "name": "07 - Delete User",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/users/{{normalUserId}}"
          }
        }
      ]
    },
    {
      "name": "Class",
      "item": [
        {
          "name": "01 - Create Class",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/classes",
            "body":{"mode":"raw","raw":"{\"title\":\"Yoga\",\"category\":\"Wellness\",\"description\":\"Intro\",\"startTime\":\"2025-05-01T10:00\",\"endTime\":\"2025-05-01T11:00\",\"capacity\":20,\"imageUrl\":\"\",\"coachId\":{{coachUserId}}}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('classId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get Classes",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/classes?keyword=&page=0&size=10"
          }
        },
        {
          "name": "03 - Get Class By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/classes/{{classId}}"
          }
        },
        {
          "name": "04 - Update Class",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/classes/{{classId}}",
            "body":{"mode":"raw","raw":"{\"title\":\"Yoga Adv\",\"category\":\"Wellness\",\"description\":\"Adv\",\"startTime\":\"2025-05-02T10:00\",\"endTime\":\"2025-05-02T11:30\",\"capacity\":15,\"imageUrl\":\"\",\"coachId\":{{coachUserId}}}"}
          }
        },
        {
          "name": "05 - Delete Class",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/classes/{{classId}}"
          }
        }
      ]
    },
    {
      "name": "Workout",
      "item": [
        {
          "name": "01 - Create Workout",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/workouts",
            "body":{"mode":"raw","raw":"{\"name\":\"Boxing\",\"category\":\"Cardio\",\"description\":\"Punch\",\"durationInMinutes\":45,\"coachId\":{{coachUserId}}}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('workoutId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get Workouts",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/workouts?keyword=&page=0&size=10"
          }
        },
        {
          "name": "03 - Get Workout By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/workouts/{{workoutId}}"
          }
        },
        {
          "name": "04 - Update Workout",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/workouts/{{workoutId}}",
            "body":{"mode":"raw","raw":"{\"name\":\"Boxing Pro\",\"category\":\"Cardio\",\"description\":\"KO\",\"durationInMinutes\":60,\"coachId\":{{coachUserId}}}"}
          }
        },
        {
          "name": "05 - Delete Workout",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/workouts/{{workoutId}}"
          }
        }
      ]
    },
    {
      "name": "Product",
      "item": [
        {
          "name": "01 - Create Product",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/products",
            "body":{"mode":"raw","raw":"{\"name\":\"Mat\",\"category\":\"Gear\",\"description\":\"Yoga Mat\",\"price\":19.99,\"stock\":50,\"imageUrl\":\"\"}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('productId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get Products",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/products?keyword=&page=0&size=10"
          }
        },
        {
          "name": "03 - Get Product By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/products/{{productId}}"
          }
        },
        {
          "name": "04 - Update Product",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/products/{{productId}}",
            "body":{"mode":"raw","raw":"{\"name\":\"Mat Pro\",\"category\":\"Gear\",\"description\":\"Premium\",\"price\":29.99,\"stock\":45,\"imageUrl\":\"\"}"}
          }
        },
        {
          "name": "05 - Get Low Stock",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/products/low-stock?threshold=100&page=0&size=10"
          }
        },
        {
          "name": "06 - Update Stock",
          "request": {
            "method":"PUT",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/products/{{productId}}/stock?quantity=5"
          }
        },
        {
          "name": "07 - Delete Product",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/products/{{productId}}"
          }
        }
      ]
    },
    {
      "name": "Booking",
      "item": [
        {
          "name": "01 - Create Booking",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/bookings",
            "body":{"mode":"raw","raw":"{\"userId\":{{normalUserId}},\"classId\":{{classId}}}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('bookingId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get All Bookings",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/bookings"
          }
        },
        {
          "name": "03 - Get By User",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/bookings/user/{{normalUserId}}"
          }
        },
        {
          "name": "04 - Update Booking",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/bookings/{{bookingId}}",
            "body":{"mode":"raw","raw":"{\"userId\":{{normalUserId}},\"classId\":{{classId}},\"status\":\"CONFIRMED\",\"bookingDate\":\"2025-05-01T09:00\"}"}
          }
        },
        {
          "name": "05 - Delete Booking",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/bookings/{{bookingId}}"
          }
        }
      ]
    },
    {
      "name": "Order",
      "item": [
        {
          "name": "01 - Create Order",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/orders",
            "body":{"mode":"raw","raw":"{\"userId\":{{normalUserId}},\"items\":[{\"productId\":{{productId}},\"quantity\":1}]}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('orderId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get Order By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/orders/{{orderId}}"
          }
        },
        {
          "name": "03 - Update Order",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/orders/{{orderId}}",
            "body":{"mode":"raw","raw":"{\"userId\":{{normalUserId}},\"items\":[{\"productId\":{{productId}},\"quantity\":2}]}"}
          }
        },
        {
          "name": "04 - Delete Order",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/orders/{{orderId}}"
          }
        }
      ]
    },
    {
      "name": "Payment",
      "item": [
        {
          "name": "01 - Create Payment",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/payments",
            "body":{"mode":"raw","raw":"{\"orderId\":{{orderId}},\"amount\":19.99,\"paymentDate\":\"2025-05-01T12:00\",\"status\":\"COMPLETED\"}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('paymentId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get All Payments",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/payments"
          }
        },
        {
          "name": "03 - Get Payment By ID",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/payments/{{paymentId}}"
          }
        },
        {
          "name": "04 - Update Payment",
          "request": {
            "method":"PUT",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/payments/{{paymentId}}",
            "body":{"mode":"raw","raw":"{\"orderId\":{{orderId}},\"amount\":29.99,\"paymentDate\":\"2025-05-01T13:00\",\"status\":\"PENDING\"}"}
          }
        },
        {
          "name": "05 - Delete Payment",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/payments/{{paymentId}}"
          }
        }
      ]
    },
    {
      "name": "Review",
      "item": [
        {
          "name": "01 - Create Review",
          "request": {
            "method":"POST",
            "header":[
              {"key":"Content-Type","value":"application/json"},
              {"key":"Authorization","value":"Bearer {{token}}"}
            ],
            "url":"{{baseUrl}}/api/reviews",
            "body":{"mode":"raw","raw":"{\"userId\":{{normalUserId}},\"workoutId\":{{workoutId}},\"rating\":5,\"comment\":\"Great!\"}"}
          },
          "event":[{"listen":"test","script":{"exec":[
            "pm.environment.set('reviewId', pm.response.json().id);"
          ]}}]
        },
        {
          "name": "02 - Get Reviews By Workout",
          "request": {
            "method":"GET",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/reviews/workout/{{workoutId}}?page=0&size=10"
          }
        },
        {
          "name": "03 - Delete Review",
          "request": {
            "method":"DELETE",
            "header":[{"key":"Authorization","value":"Bearer {{token}}"}],
            "url":"{{baseUrl}}/api/reviews/{{reviewId}}"
          }
        }
      ]
    }
  ]
}
