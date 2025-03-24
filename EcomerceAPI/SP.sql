CREATE PROCEDURE ProcessOrder(p_user_id INT)
    LANGUAGE plpgsql
AS $$
DECLARE
v_order_id INT;
    v_total_price DECIMAL(10,2);
BEGIN

BEGIN

        -- Calcular el total del carrito
SELECT COALESCE(SUM(p.price * c.quantity), 0) INTO v_total_price
FROM cartitem c
         JOIN product p ON p.product_id = c.product_id
WHERE c.user_id = p_user_id;

-- Insertar en 'order' y obtener el nuevo ID
INSERT INTO "order" (user_id, totalPrice, status)
VALUES (p_user_id, v_total_price, 'pending')
    RETURNING order_id INTO v_order_id;

-- Insertar ítems del carrito en 'order_item'
INSERT INTO order_item (order_id, product_id, quantity, price)
SELECT v_order_id, c.product_id, c.quantity, p.price
FROM cartitem c
         JOIN product p ON p.product_id = c.product_id
WHERE c.user_id = p_user_id;

-- Actualizar el stock de los productos
update product set stock = stock - c.quantity  from cartitem c
where product.product_id = c.product_id and c.user_id = p_user_id;

-- Eliminar los productos del carrito
DELETE FROM cartitem WHERE user_id = p_user_id;


-- Confirmar transacción
COMMIT;

EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
END;

END $$;