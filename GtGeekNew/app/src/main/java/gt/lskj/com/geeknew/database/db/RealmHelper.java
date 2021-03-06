package gt.lskj.com.geeknew.database.db;

import android.content.Context;

import java.util.List;

import gt.lskj.com.geeknew.database.bean.ReadStateBean;
import gt.lskj.com.geeknew.database.bean.RealmLikeBean;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by codeest on 16/8/16.
 */

public class RealmHelper {

    private static final String DB_NAME = "myRealm.realm";

    private Realm mRealm;

    public RealmHelper(Context mContext) {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder(mContext)
                .deleteRealmIfMigrationNeeded()
                .name(DB_NAME)
                .build());
    }

    /**
     * 增加 阅读记录
     * @param id
     */
    public void insertNewsId(int id) {
        mRealm.beginTransaction();
        ReadStateBean bean = mRealm.createObject(ReadStateBean.class);
        bean.setId(id);
        mRealm.commitTransaction();
    }

    /**
     * 查询 阅读记录
     * @param id
     * @return
     */
    public boolean queryNewsId(int id) {
        RealmResults<ReadStateBean> results = mRealm.where(ReadStateBean.class).findAll();
        for(ReadStateBean item : results) {
            if(item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * 增加 收藏记录
     * @param bean
     */
    public void insertLikeBean(RealmLikeBean bean) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(bean);
        mRealm.commitTransaction();
    }

    /**
     * 删除 收藏记录
     * @param id
     */
    public void deleteLikeBean(String id) {
        RealmLikeBean data = mRealm.where(RealmLikeBean.class).equalTo("id",id).findFirst();
        mRealm.beginTransaction();
        data.deleteFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * 查询 收藏记录
     * @param id
     * @return
     */
    public boolean queryLikeId(String id) {
        RealmResults<RealmLikeBean> results = mRealm.where(RealmLikeBean.class).findAll();
        for(RealmLikeBean item : results) {
            if(item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public List<RealmLikeBean> getLikeList() {
        //使用findAllSort ,先findAll再result.sort无效
        RealmResults<RealmLikeBean> results = mRealm.where(RealmLikeBean.class).findAllSorted("time");
        return mRealm.copyFromRealm(results);
    }

    /**
     * 修改 收藏记录 时间戳以重新排序
     * @param id
     * @param time
     * @param isPlus
     */
    public void changeLikeTime(String id ,long time, boolean isPlus) {
        RealmLikeBean bean = mRealm.where(RealmLikeBean.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        if (isPlus) {
            bean.setTime(++time);
        } else {
            bean.setTime(--time);
        }
        mRealm.commitTransaction();
    }
}
