package com.denisbondd111.tacocloud.repository;

import com.denisbondd111.tacocloud.domain.IngredientRef;
import com.denisbondd111.tacocloud.domain.Taco;
import com.denisbondd111.tacocloud.domain.TacoOrder;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcOrderRepository implements OrderRepository{
    //language=SQL
    private final String SQL_SAVE_ORDER = "insert into Taco_Order (delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at) values (?,?,?,?,?,?,?,?,?)";
    //language=SQL
    private final String SQL_SAVE_TACO = "insert into Taco (name, created_at, taco_order, taco_order_key) values (?, ?, ?, ?)";
    //language=SQL
    private final String SQL_SAVE_INGREDIENT_REF = "insert into Ingredient_Ref (ingredient, taco, taco_key) values (?, ?, ?)";
    private JdbcOperations jdbcOperations;

    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                SQL_SAVE_ORDER,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);
        tacoOrder.setPlacedAt(new Date());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        tacoOrder.getDeliveryName(),
                        tacoOrder.getDeliveryStreet(),
                        tacoOrder.getDeliveryCity(),
                        tacoOrder.getDeliveryState(),
                        tacoOrder.getDeliveryZip(),
                        tacoOrder.getCcNumber(),
                        tacoOrder.getCcExpiration(),
                        tacoOrder.getCcCVV(),
                        tacoOrder.getPlacedAt())
        );
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);
        List<Taco> tacos = tacoOrder.getTacos();

        int i = 0;
        for(Taco taco : tacos){
            saveTaco(orderId, i++, taco);
        }
        return tacoOrder;
    }

    private long saveTaco(long orderId, int orderKey, Taco taco){
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                SQL_SAVE_TACO,
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getCreatedAt(),
                        orderId,
                        orderKey)
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients().stream().map(ingredient -> new IngredientRef(ingredient.getId())).collect(Collectors.toList()));

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs){
        int key = 0;
        for(IngredientRef ingredientRef : ingredientRefs){
            jdbcOperations.update(
                    SQL_SAVE_INGREDIENT_REF,
                    ingredientRef.getIngredient(), tacoId, key++
            );
        }
    }
}
