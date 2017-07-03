CREATE TABLE contas
(
id SERIAL PRIMARY KEY,
codigo VARCHAR(255) NOT NULL,
conta VARCHAR(255),
pai INTEGER,
tipoconta VARCHAR(255) NOT NULL,
valor NUMERIC(10, 4) NOT NULL,
FOREIGN KEY (pai)
REFERENCES contas(id) MATCH SIMPLE
ON DELETE CASCADE
);

CREATE FUNCTION getAllTree(param_id BIGINT) RETURNS SETOF contas AS $$
DECLARE
r contas;
BEGIN

FOR r IN SELECT * FROM contas c WHERE c.pai = param_id LOOP
RETURN NEXT r;
RETURN QUERY SELECT * FROM getAllTree(r.id);
END LOOP;
return;
END;
$$
LANGUAGE PLPGSQL;

CREATE FUNCTION calcularValores()
RETURNS TRIGGER AS $$
DECLARE
valorFinal NUMERIC(10, 4);
r contas%rowtype;
BEGIN
valorFinal := 0;
IF NEW.tipoconta = 'SINTETICA' THEN 
FOR r IN (SELECT * FROM contas c WHERE c.pai = NEW.id) LOOP
valorFinal := valorFinal + r.valor;
END LOOP;
NEW.valor := valorFinal;
ELSE END IF;
RETURN NEW;
END;
$$
LANGUAGE PLPGSQL;

CREATE TRIGGER atualizarValoresSinteticos BEFORE UPDATE ON contas FOR EACH ROW EXECUTE PROCEDURE calcularValores();