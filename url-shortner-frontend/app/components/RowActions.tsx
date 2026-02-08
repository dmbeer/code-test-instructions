interface RowActionsProps {
    item: URLSResponse;
}

const RowActions: React.FC<RowActionsProps> = ({ item }) => {
    const handleCopy = async () => {
        await navigator.clipboard.writeText(item.shortUrl);
        alert("Copied!");
    };

    const handleDelete = () => {
        // confirm + API call
        console.log("Delete", item.alias);
    };

    return (
        <div className="flex gap-2">
            <button
                className="text-red-600 hover:underline"
                onClick={handleDelete}
            >
                Delete
            </button>
        </div>
    );
};

export default RowActions;
