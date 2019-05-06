### Scout 24 Tech Challenge

#### Run Test
```sbt test``` to run all tests

#### Start docker image running Postgres 11.2
```cd docker && docker-compose up -d```

#### Run the applicaiton
```sbt run``` and go have some fun either using curl or httpie with /v1/caradverts endpoint :-)

```
http --verbose POST http://localhost:9000/v1/caradverts "title"="Panda 1.2" "fuel"="gasoline" "price"=6500 "new"=false mileage=87000 "first registration"="2013-04-12"

http --verbose POST http://localhost:9000/v1/caradverts "title"="Golf" "fuel"="diesel" "price"=5500 "new"=true

http --verbose POST http://localhost:9000/v1/caradverts "title"="Audi A4 Avant" "fuel"="gasoline" "price"=2500 "new"=false "first registration"="2002-01-01" mileage=200

http --verbose GET http://localhost:9000/v1/caradverts?sortBy=title

http --verbose GET http://localhost:9000/v1/caradverts?sortBy=price

http --verbose GET http://localhost:9000/v1/caradverts?sortBy=new

http --verbose GET http://localhost:9000/v1/caradverts
```