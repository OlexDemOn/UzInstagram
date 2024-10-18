import React from "react";
import { twMerge } from "tailwind-merge";

const Footer = React.forwardRef<
    HTMLDivElement,
    React.HTMLProps<HTMLDivElement>
>((props, ref) => {
    return (
        <footer
            className={twMerge(
                "flex items-center justify-center p-6 bg-background",
                props.className
            )}
            ref={ref}
            {...props}
        >
            <span className="font-semibold">Footer</span>
        </footer>
    );
});

export default Footer;
