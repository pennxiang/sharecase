package com.sharedcase.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sharedcase.entity.CaseDetail;
import com.sharedcase.entity.CaseVersion;
import com.sharedcase.entity.HotDiseaseDTO;
import com.sharedcase.util.IpfsUtil;
import com.sharedcase.util.PdfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: CaseDetailService
 * Package: com.sharedcase.service
 * Description:
 *      病例服务
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:20
 */
@Service
public class CaseService {

    private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

    @Autowired
    private IpfsService ipfsService;

    /**
     * 上传检查报告
     */
    public List<String> uploadCheckReport(String caseId, List<File> file) {
        try {
            List<String> infoAttachments = new ArrayList<>();
            // 1. 上传到 IPFS
            for (File readyfile : file) {
                String ipfsHash = IpfsUtil.upload(readyfile, "/cases/" + caseId);
                infoAttachments.add(ipfsHash);
            }
            // 2. 写入区块链
            // TODO

            return infoAttachments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * 创建病例
     */
    public String createCase(CaseDetail caseDetail) {
        // 1. 生成 PDF
        String filename = caseDetail.getCaseId() + ".pdf";
        File file = new File(filename);
        try {
            // PDF 渲染
            PdfUtil.exportCaseDetailToPdf(caseDetail, file.getAbsolutePath());
            // 2. 上传到 IPFS
            String ipfsHash = IpfsUtil.upload(file, "/cases");
            // 3. 写入区块链, 新建病例版本，需要根据传入的icd拼versionCode
            // TODO

            return ipfsHash;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * 更新病例信息
     * 每次更新实质上是将版本号进行更新，并不会实际改变存储在ipfs上的病例内容，返回结果依旧是ipfs的hash
     */
    public String updateCase(CaseDetail caseDetail){
        // 1. 生成 PDF
        String filename = caseDetail.getCaseId() + ".pdf";
        File file = new File(filename);
        try {
            // PDF 渲染
            PdfUtil.exportCaseDetailToPdf(caseDetail, file.getAbsolutePath());
            // 2. 上传到 IPFS
            String ipfsHash = IpfsUtil.upload(file, "/cases");
            // 3. 写入区块链
            // TODO
            // 更新版本号


            return ipfsHash;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * 根据ipfs hash获取病例信息
     */
    public CaseDetail getCaseByHash(String ipfsHash) {
        try {
            // 1. 从 IPFS 下载 PDF 文件（保存到临时目录）
            File pdfFile = IpfsUtil.download(ipfsHash, "/cases/" + ipfsHash + ".pdf", "tmp");

            CaseDetail caseDetail = PdfUtil.parsePdfToCaseDetail(pdfFile);

            boolean success = pdfFile.delete();
            if (!success) {
                // 删除失败
                logger.warn("临时文件删除失败：" + pdfFile.getAbsolutePath());
            }
            // 2. 解析 PDF → 提取字段 → 构建 CaseDetail 对象
            return caseDetail;

        } catch (Exception e) {
            logger.warn("解析病例失败，CID: " + ipfsHash, e);
            throw new RuntimeException("解析病例失败，CID: " + ipfsHash, e);
        }
    }

}
