DROP TABLE IF EXISTS apartments;

CREATE TABLE apartments
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  rent integer NOT NULL,
  number_of_bedrooms integer NOT NULL,
  number_of_bathrooms numeric(4, 2) NOT NULL,
  square_footage integer NOT NULL,
  address VARCHAR(255),
  city VARCHAR(255),
  state VARCHAR(20),
  zip_code VARCHAR(10),
  user_id BIGINT,
  is_active boolean not null default false,
  number_of_likes integer not null
);