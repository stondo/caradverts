# --- !Ups

CREATE TABLE "CARADVERT" (
    "id" bigserial NOT NULL,
    "title" text NOT NULL,
    "fuel" text NOT NULL,
    "price" integer NOT NULL,
    "new" boolean NOT NULL,
    "mileage" integer NULL,
    "first registration" date NULL,
    "createdAt" timestamp NOT NULL,
    "updatedAt" timestamp NULL,
    PRIMARY KEY ("id")
);

# --- !Downs

DROP TABLE IF EXISTS "CARADVERT" CASCADE;