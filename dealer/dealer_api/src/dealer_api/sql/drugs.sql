-- :name drugs :? :*
-- :doc Get all drugs
SELECT * FROM drugs;

-- :name new-drug :insert :1
INSERT INTO
drugs(name, availability, price)
VALUES(:name, :availability, :price)
RETURNING id;

-- :name drug :? :1
SELECT * from drugs WHERE id=:id;

-- :name update-drug :? :1
UPDATE drugs SET 

-- :name delete-drug :? :1
DELETE FROM drugs WHERE id=:id;
