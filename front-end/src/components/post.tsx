import { TPost } from "types/post";

const Post = ({ post }: { post: TPost }) => {
    console.log(post);
    return (
        <div className="border max-w-80 w-80">
            <div className="border bg-yellow-500">{post.title}</div>
            <div className="h-40 bg-purple-500"></div>
            <div className="h-10 bg-green-500">{post.body}</div>
        </div>
    );
};

export default Post;
