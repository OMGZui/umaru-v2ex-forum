package com.example.v2exclone.config;

import com.example.v2exclone.entity.Node;
import com.example.v2exclone.entity.Topic;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.repository.NodeRepository;
import com.example.v2exclone.repository.TopicRepository;
import com.example.v2exclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NodeRepository nodeRepository;
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 创建用户
        User user1 = new User("小埋酱", "umaru@example.com", "password123");
        user1.setAvatar("https://i.imgur.com/8QmIp.png");
        user1.setBio("干物妹小埋，喜欢游戏和可乐");
        userRepository.save(user1);

        User user2 = new User("干物妹小埋", "himouto@example.com", "password123");
        user2.setAvatar("https://i.imgur.com/VQWPsBS.png");
        user2.setBio("在家就是干物妹模式");
        userRepository.save(user2);

        User user3 = new User("うまるちゃん", "umaru_jp@example.com", "password123");
        user3.setAvatar("https://i.imgur.com/kH25Y.png");
        user3.setBio("ゲームとコーラが大好き");
        userRepository.save(user3);

        User user4 = new User("土间埋同学", "doma_umaru@example.com", "password123");
        user4.setAvatar("https://i.imgur.com/2QoLk.png");
        user4.setBio("学校では優等生、家では干物妹");
        userRepository.save(user4);

        User user5 = new User("UMR大神", "umr@example.com", "password123");
        user5.setAvatar("https://i.imgur.com/9QmIp.png");
        user5.setBio("游戏高手UMR");
        userRepository.save(user5);

        // 创建节点
        Node techNode = new Node("技术", "tech", "技术相关讨论");
        nodeRepository.save(techNode);
        
        Node shareNode = new Node("分享创造", "share", "分享你的创造和发现");
        nodeRepository.save(shareNode);
        
        Node appleNode = new Node("Apple", "apple", "Apple 产品讨论");
        nodeRepository.save(appleNode);
        
        Node qaNode = new Node("问与答", "qna", "问题和答案");
        nodeRepository.save(qaNode);
        
        Node startupNode = new Node("创业", "startup", "创业相关话题");
        nodeRepository.save(startupNode);
        
        // 创建主题
        Topic topic1 = new Topic(
            "如何在 2024 年成为一名优秀的前端开发者？",
            "随着技术的快速发展，前端开发的要求也越来越高。想请教大家，在 2024 年，一名优秀的前端开发者需要掌握哪些技能？",
            user1,
            techNode
        );
        topic1.setReplyCount(23);
        topic1.setViewCount(156);
        topicRepository.save(topic1);
        
        Topic topic2 = new Topic(
            "分享一个我用 Python 写的小工具，可以自动整理桌面文件",
            "最近写了一个 Python 脚本，可以根据文件类型自动整理桌面文件到不同的文件夹。代码已经开源到 GitHub，欢迎大家使用和改进。",
            user2,
            shareNode
        );
        topic2.setReplyCount(15);
        topic2.setViewCount(89);
        topicRepository.save(topic2);
        
        Topic topic3 = new Topic(
            "MacBook Pro M3 使用体验分享，性能提升明显",
            "刚入手了新的 MacBook Pro M3，使用了一周后来分享一下体验。相比之前的 Intel 版本，性能提升确实很明显，特别是在视频编辑和编程方面。",
            user3,
            appleNode
        );
        topic3.setReplyCount(42);
        topic3.setViewCount(234);
        topicRepository.save(topic3);
        
        Topic topic4 = new Topic(
            "求推荐一款好用的代码编辑器，主要用于 Web 开发",
            "最近在学习 Web 开发，想找一款好用的代码编辑器。目前在 VS Code 和 WebStorm 之间纠结，大家有什么推荐吗？",
            user4,
            qaNode
        );
        topic4.setReplyCount(67);
        topic4.setViewCount(178);
        topicRepository.save(topic4);
        
        Topic topic5 = new Topic(
            "创业公司技术选型的一些思考和建议",
            "作为一个技术创业者，分享一下在技术选型方面的经验。选择技术栈时不仅要考虑技术本身，还要考虑团队能力、招聘难度、维护成本等因素。",
            user5,
            startupNode
        );
        topic5.setReplyCount(31);
        topic5.setViewCount(145);
        topicRepository.save(topic5);
        
        System.out.println("数据初始化完成！");
    }
}
