package com.project.zzimccong.model.entity.store;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurant is a Querydsl query type for Restaurant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurant extends EntityPathBase<Restaurant> {

    private static final long serialVersionUID = -568519452L;

    public static final QRestaurant restaurant = new QRestaurant("restaurant");

    public final StringPath businessHours = createString("businessHours");

    public final StringPath category = createString("category");

    public final StringPath detailInfo = createString("detailInfo");

    public final StringPath facilities = createString("facilities");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final StringPath link = createString("link");

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath mainPhotoUrl = createString("mainPhotoUrl");

    public final ListPath<Menu, QMenu> menus = this.<Menu, QMenu>createList("menus", Menu.class, QMenu.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath numberAddress = createString("numberAddress");

    public final StringPath parkingInfo = createString("parkingInfo");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath photo1Url = createString("photo1Url");

    public final StringPath photo2Url = createString("photo2Url");

    public final StringPath photo3Url = createString("photo3Url");

    public final StringPath photo4Url = createString("photo4Url");

    public final StringPath photo5Url = createString("photo5Url");

    public final StringPath roadAddress = createString("roadAddress");

    public final StringPath seats = createString("seats");

    public QRestaurant(String variable) {
        super(Restaurant.class, forVariable(variable));
    }

    public QRestaurant(Path<? extends Restaurant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRestaurant(PathMetadata metadata) {
        super(Restaurant.class, metadata);
    }

}

