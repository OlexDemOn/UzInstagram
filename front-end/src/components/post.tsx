import React from "react";

const Post = React.forwardRef<HTMLDivElement, React.HTMLProps<HTMLDivElement>>(
    ({ postData }, props, ref) => {
        console.log(postData);
        return (
            <div className="">
                <h1>
                    {postData.firstName} {postData.lastName}
                </h1>
                <p>{postData.email}</p>
                <p className="text-purple-500">{postData.phone}</p>
            </div>
        );
    }
);

export default Post;
