-- :name drugs :? :*
-- :doc Get all drugs
SELECT * FROM drugs;

-- :name new-drug :? :1
INSERT INTO drugs(name, availability, price) VALUE(:name, :availability, :price);

-- :name drug :? :1
SELECT * from drugs WHERE id=:id;

-- :name update-drug :? :1
UPDATE drugs SET 

-- :name delete-drug :? :1
DELETE FROM drugs WHERE id=:id;
