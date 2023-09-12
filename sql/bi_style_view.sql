select ts.id,
       IF('衣长分类' = tfv.field_name, tfv.val_name, null)                          as 衣长分类,
       IF('衣长' = tfv.field_name, tfv.val_name, null)                              as 衣长,
       IF('袖型' = tfv.field_name, tfv.val_name, null)                              as 袖型,
       IF('袖长' = tfv.field_name, tfv.val_name, null)                              as 袖长,
       IF('腰型' = tfv.field_name, tfv.val_name, null)                              as 腰型,
       IF('胸围' = tfv.field_name, tfv.val_name, null)                              as 胸围,
       IF('门襟' = tfv.field_name, tfv.val_name, null)                              as 门襟,
       IF('毛纱针型' = tfv.field_name, tfv.val_name, null)                          as 毛纱针型,
       IF('毛纱针法' = tfv.field_name, tfv.val_name, null)                          as 毛纱针法,
       IF('廓形' = tfv.field_name, tfv.val_name, null)                              as 廓形,
       IF('花型' = tfv.field_name, tfv.val_name, null)                              as 花型,
       IF('领型' = tfv.field_name, tfv.val_name, null)                              as 领型,
       IF('材质' = tfv.field_name, tfv.val_name, null)                              as 材质,
       IF('肩宽' = tfv.field_name, tfv.val_name, null)                              as 肩宽,
       ts.style_no                                                                  as 大货款号,
       IF(ts.style_no is null or ts.style_no = '', '否', '是')                      as 是否大货,
       ts.designer                                                                  as 下稿设计师,
       ts.designer_id                                                               as 下稿设计师id,
       ts.task_level_name                                                           as 紧急状态,
       ts.brand_name                                                                as 季节品牌,
       ts.year_name                                                                 as 季节年度,
       ts.season_name                                                               as 季节季节,
       ts.design_no                                                                 as 设计款号,
       true                                                                         as 启用,
       ts.prod_category1st_name                                                     as 大类,
       ts.prod_category2nd_name                                                     as 中类,
       ts.prod_category3rd_name                                                     as 小类,
       ts.prod_category_name                                                        as 品类,
       ts.style_type_name                                                           as 款式类型,
       ts.devt_type_name                                                            as 生产类型,
       ts.style_name                                                                as 款式名称,
       ts.silhouette_name                                                           as 廓形,
       ts.designer                                                                  as 设计师,
       ts.designer_id                                                               as 设计师id,
       ts.style_no                                                                  as 款号,
       ts.merch_design_name                                                         as 跟款设计师,
       ts.merch_design_id                                                           as 跟款设计师id,
       ts.technician_name                                                           as 工艺员,
       ts.technician_id                                                             as 工艺员id,
       ts.pattern_design_name                                                       as 版师,
       ts.pattern_design_id                                                         as 版师id,
       ts.fab_develope_name                                                         as 材料专员,
       ts.fab_develope_id                                                           as 材料专员id,
       ts.actual_publication_date                                                   as 实际出稿时间,
       ts.style_unit                                                                as 单位,
       ts.dev_class_name                                                            as 开发分类,
       ts.pat_diff_name                                                             as 打版难度,
       ts.create_date                                                               as 创建时间,
       ts.create_name                                                               as 创建人,
       ts.update_date                                                               as 修改时间,
       ts.update_name                                                               as 修改人,
       ts.reviewed_design_name                                                      as 审版设计师,
       ts.reviewed_design_id                                                        as 审版设计师id,
       ts.style_origin_name                                                         as 款式来源,
       ts.style_flavour_name                                                        as 款式风格,
       ts.id                                                                        as StyleURL,
       ((select count(*) from t_pattern_making where style_id = ts.id) + (select count(*)
                                                                          from t_pre_production_sample_task
                                                                          where style_id = ts.id
                                                                          order by create_date desc
                                                                          limit 1)) as 样品数,
       ts.revised_design_name                                                       as 改款设计师,
       ts.revised_design_id                                                         as 改款设计师id,
       ts.positioning_name                                                          as 款式定位
from t_style as ts
         left join t_field_val as tfv on ts.id = tfv.foreign_id and tfv.del_flag = '0' and tfv.val_name is not null
where ts.del_flag = '0'
group by ts.id

