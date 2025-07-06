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
        // 检查是否已有数据，避免重复初始化
        if (userRepository.count() > 0) {
            System.out.println("数据已存在，跳过初始化");
            return;
        }

        // 创建用户
        User user1 = User.builder()
                .username("小埋酱")
                .email("umaru@example.com")
                .password("password123")
                .avatar("https://i.imgur.com/8QmIp.png")
                .bio("干物妹小埋，喜欢游戏和可乐")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("干物妹小埋")
                .email("himouto@example.com")
                .password("password123")
                .avatar("https://i.imgur.com/6VBx3.png")
                .bio("在家就是干物妹模式")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .username("うまるちゃん")
                .email("umaru_jp@example.com")
                .password("password123")
                .avatar("https://i.imgur.com/kH25Y.png")
                .bio("ゲームとコーラが大好き")
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .username("土间埋同学")
                .email("doma_umaru@example.com")
                .password("password123")
                .avatar("https://i.imgur.com/2QoLk.png")
                .bio("学校では優等生、家では干物妹")
                .build();
        userRepository.save(user4);

        User user5 = User.builder()
                .username("UMR大神")
                .email("umr@example.com")
                .password("password123")
                .avatar("https://i.imgur.com/9QmIp.png")
                .bio("游戏高手UMR")
                .build();
        userRepository.save(user5);

        // 创建节点
        Node techNode = Node.builder()
                .name("技术")
                .slug("tech")
                .description("技术相关讨论")
                .build();
        nodeRepository.save(techNode);

        Node shareNode = Node.builder()
                .name("分享创造")
                .slug("share")
                .description("分享你的创造和发现")
                .build();
        nodeRepository.save(shareNode);

        Node appleNode = Node.builder()
                .name("Apple")
                .slug("apple")
                .description("Apple 产品讨论")
                .build();
        nodeRepository.save(appleNode);

        Node qaNode = Node.builder()
                .name("问与答")
                .slug("qna")
                .description("问题和答案")
                .build();
        nodeRepository.save(qaNode);

        Node startupNode = Node.builder()
                .name("创业")
                .slug("startup")
                .description("创业相关话题")
                .build();
        nodeRepository.save(startupNode);
        
        // 创建主题
        Topic topic1 = Topic.builder()
                .title("如何在 2024 年成为一名优秀的前端开发者？")
                .content("随着技术的快速发展，前端开发的要求也越来越高。想请教大家，在 2024 年，一名优秀的前端开发者需要掌握哪些技能？")
                .author(user1)
                .node(techNode)
                .replyCount(23)
                .viewCount(156)
                .build();
        topicRepository.save(topic1);

        Topic topic2 = Topic.builder()
                .title("分享一个我用 Python 写的小工具，可以自动整理桌面文件")
                .content("最近写了一个 Python 脚本，可以根据文件类型自动整理桌面文件到不同的文件夹。代码已经开源到 GitHub，欢迎大家使用和改进。")
                .author(user2)
                .node(shareNode)
                .replyCount(15)
                .viewCount(89)
                .build();
        topicRepository.save(topic2);
        
        Topic topic3 = Topic.builder()
                .title("MacBook Pro M3 使用体验分享，性能提升明显")
                .content("刚入手了新的 MacBook Pro M3，使用了一周后来分享一下体验。相比之前的 Intel 版本，性能提升确实很明显，特别是在视频编辑和编程方面。")
                .author(user3)
                .node(appleNode)
                .replyCount(42)
                .viewCount(234)
                .build();
        topicRepository.save(topic3);

        Topic topic4 = Topic.builder()
                .title("求推荐一款好用的代码编辑器，主要用于 Web 开发")
                .content("最近在学习 Web 开发，想找一款好用的代码编辑器。目前在 VS Code 和 WebStorm 之间纠结，大家有什么推荐吗？")
                .author(user4)
                .node(qaNode)
                .replyCount(67)
                .viewCount(178)
                .build();
        topicRepository.save(topic4);

        Topic topic5 = Topic.builder()
                .title("创业公司技术选型的一些思考和建议")
                .content("作为一个技术创业者，分享一下在技术选型方面的经验。选择技术栈时不仅要考虑技术本身，还要考虑团队能力、招聘难度、维护成本等因素。")
                .author(user5)
                .node(startupNode)
                .replyCount(31)
                .viewCount(145)
                .build();
        topicRepository.save(topic5);
        
        System.out.println("数据初始化完成！");
    }
}
