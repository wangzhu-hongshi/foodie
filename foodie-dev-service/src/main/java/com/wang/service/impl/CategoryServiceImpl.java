package com.wang.service.impl;

        import com.imooc.enums.CategoryEnum;
        import com.wang.mapper.CategoryMapper;
        import com.wang.mapper.CategoryMapperCustom;
        import com.wang.pojo.Category;
        import com.wang.service.CategoryService;
        import com.wang.vo.CategoryVo;
        import com.wang.vo.NewItemsVo;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Propagation;
        import org.springframework.transaction.annotation.Transactional;
        import tk.mybatis.mapper.entity.Example;

        import java.util.List;

/**
 * 分类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    /**
     * 查看一级分类
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRooLevelCat() {
        Example example=new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", CategoryEnum.Root.type);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVo> getSubCatList(Integer rootCatId) {
        List<CategoryVo> subCatList = categoryMapperCustom.getSubCatList(rootCatId);
        return subCatList;
    }

    /**
     * 查看每个一级分类的最新6个商品数据
     * @param rootCatId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVo> getSixNewItemsLazy(Integer rootCatId) {
        List<NewItemsVo> itemsLazy = categoryMapperCustom.getSixNewItemsLazy(rootCatId);
        return itemsLazy;
    }
}
