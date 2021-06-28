<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${Configuration.packageName}.${Configuration.path.dao}.${MapperClassName}">

    <resultMap id="${ClassAttrName}ResultMap" type="${Configuration.packageName}.${Configuration.path.entity}.${ClassName}">
        ${ResultMap}
        ${Association}
        ${Collection}
    </resultMap>

    <sql id="${ClassAttrName}Columns">
        ${ColumnMap}
    </sql>

    <sql id="${ClassAttrName}Joins">
        ${Joins}
    </sql>

    <select id="get" resultMap="${ClassAttrName}ResultMap">
        SELECT
        <include refid="${ClassAttrName}Columns" />
        FROM `${TableName}` <include refid="${ClassAttrName}Joins" />
        <where>
            `${TableName}`.`${PrimaryColumn.columnName}` = ${r"#{"}${PrimaryColumn.propertyName}${r"}"}
        </where>
    </select>

    <select id="findAll" resultMap="${ClassAttrName}ResultMap">
        SELECT
        <include refid="${ClassAttrName}Columns" />
        FROM `${TableName}` <include refid="${ClassAttrName}Joins" />
        <where>
        </where>
    </select>

    <insert id="insert">
        INSERT INTO `${TableName}`(
            ${InsertProperties}
        )
        VALUES (
            ${InsertValues}
        )
    </insert>

    <insert id="insertBatch">
        INSERT INTO ${TableName}(
            ${InsertProperties}
        )
        VALUES
        <foreach collection ="list" item="${ClassAttrName}" separator =",">
        (
            ${InsertBatchValues}
        )
        </foreach>
    </insert>

    <update id="update">
        UPDATE `${TableName}` SET
        ${UpdateProperties}
        WHERE `${PrimaryColumn.columnName}` = ${r"#{"}${PrimaryColumn.propertyName}${r"}"}
    </update>

    <update id="delete">
        DELETE FROM `${TableName}`
        WHERE `${PrimaryColumn.columnName}` = ${r"#{"}${PrimaryColumn.propertyName}${r"}"}
    </update>

</mapper>