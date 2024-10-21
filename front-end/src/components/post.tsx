import { TPost } from "types/post";

const Post = ({ post }: { post: TPost }) => {
    console.log(post);
    return (
        <div>
            <p>{post.title}</p>
            <p>{post.body}</p>
        </div>
    );
};

export default Post;
