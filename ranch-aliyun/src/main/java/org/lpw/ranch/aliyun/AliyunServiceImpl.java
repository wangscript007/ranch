package org.lpw.ranch.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadURLStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadURLStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * @author lpw
 */
@Service(AliyunModel.NAME + ".service")
public class AliyunServiceImpl implements AliyunService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private Generator generator;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private AliyunDao aliyunDao;
    private ExecutorService executorService = Executors.newFixedThreadPool(64);
    private Map<String, IAcsClient> map = new ConcurrentHashMap<>();

    @Override
    public JSONObject query() {
        return aliyunDao.query(pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void save(AliyunModel aliyun) {
        AliyunModel model = aliyunDao.find(aliyun.getKey());
        aliyun.setId(model == null ? null : model.getId());
        aliyunDao.save(aliyun);
        map.remove(aliyun.getKey());
    }

    @Override
    public void delete(String id) {
        AliyunModel aliyun = aliyunDao.findById(id);
        if (aliyun == null)
            return;

        aliyunDao.delete(aliyun);
        map.remove(aliyun.getKey());
    }

    @Override
    public String uploadVideo(String key, String title, String file) {
        AliyunModel aliyun = aliyunDao.find(key);
        if (aliyun == null)
            return null;

        UploadVideoResponse uploadURLStreamResponse = new UploadVideoImpl().uploadVideo(new UploadVideoRequest(
                aliyun.getAccessKeyId(), aliyun.getAccessKeySecret(), title, file));
        if (uploadURLStreamResponse.isSuccess())
            return uploadURLStreamResponse.getVideoId();

        logger.warn(null, "上传视频文件[{}:{}:{}]到阿里云失败[{}:{}]！", key, title, file,
                uploadURLStreamResponse.getCode(), uploadURLStreamResponse.getMessage());

        return null;
    }

    @Override
    public void uploadVideo(String key, String title, String file, String id, BiConsumer<String, String> biConsumer) {
        executorService.submit(() -> biConsumer.accept(id, uploadVideo(key, title, file)));
    }

    @Override
    public String uploadVideo(String key, String title, String fileName, String url) {
        AliyunModel aliyun = aliyunDao.find(key);
        if (aliyun == null)
            return null;

        UploadURLStreamResponse uploadURLStreamResponse = new UploadVideoImpl().uploadURLStream(new UploadURLStreamRequest(
                aliyun.getAccessKeyId(), aliyun.getAccessKeySecret(), title, fileName, url));
        if (uploadURLStreamResponse.isSuccess())
            return uploadURLStreamResponse.getVideoId();

        logger.warn(null, "上传视频文件[{}:{}:{}:{}]到阿里云失败[{}:{}]！", key, title, fileName, url,
                uploadURLStreamResponse.getCode(), uploadURLStreamResponse.getMessage());

        return null;
    }

    @Override
    public void uploadVideo(String key, String title, String fileName, String url, String id, BiConsumer<String, String> biConsumer) {
        executorService.submit(() -> biConsumer.accept(id, uploadVideo(key, title, fileName, url)));
    }

    @Override
    public String getVideoUrl(String key, String videoId) {
        IAcsClient acsClient = getIAcsClient(key);
        if (acsClient == null)
            return null;

        GetPlayInfoRequest getPlayInfoRequest = new GetPlayInfoRequest();
        getPlayInfoRequest.setVideoId(videoId);
        try {
            List<GetPlayInfoResponse.PlayInfo> playInfoList = acsClient.getAcsResponse(getPlayInfoRequest).getPlayInfoList();
            if (validator.isEmpty(playInfoList))
                return null;

            return playInfoList.get(generator.random(0, playInfoList.size() - 1)).getPlayURL();
        } catch (Throwable throwable) {
            logger.warn(throwable, "获取视频播放URL地址时发生异常！");

            return null;
        }
    }

    private IAcsClient getIAcsClient(String key) {
        return map.computeIfAbsent(key, k -> {
            AliyunModel aliyun = aliyunDao.find(k);

            return aliyun == null ? null :
                    new DefaultAcsClient(DefaultProfile.getProfile(aliyun.getRegionId(), aliyun.getAccessKeyId(), aliyun.getAccessKeySecret()));
        });
    }
}
